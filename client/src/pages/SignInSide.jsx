import * as React from 'react';
import { useController, useForm } from 'react-hook-form';
import { useLocation, useNavigate } from 'react-router-dom';
import { zodResolver } from '@hookform/resolvers/zod';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Unstable_Grid2';
import PropTypes from 'prop-types';

import signInImgUrl from '../assets/sign-in-01.jpg';
import { AuthFooter } from '../components';
import useAuthentication from '../hooks/useAuthentication';
import loginSchema from '../schemas/loginSchema';

function ControlledTextField({
  name,
  control,
  loginError,
  label,
  autoComplete,
  type = 'text',
  autoFocus = false,
}) {
  const {
    field: { ref, ...inputProps },
    fieldState: { error },
  } = useController({
    name,
    control,
  });

  return (
    <TextField
      {...inputProps}
      inputRef={ref}
      error={!!error || !!loginError}
      helperText={error?.message || loginError || ''}
      required
      fullWidth
      id={`sign-in-${name}`}
      label={label}
      autoComplete={autoComplete}
      type={type}
      autoFocus={autoFocus}
      variant='filled'
      margin='normal'
    />
  );
}

ControlledTextField.propTypes = {
  name: PropTypes.string.isRequired,
  control: PropTypes.object.isRequired,
  loginError: PropTypes.string,
  label: PropTypes.string.isRequired,
  autoComplete: PropTypes.string,
  type: PropTypes.string,
  autoFocus: PropTypes.bool,
};

export default function SignInSide() {
  const [loginError, setLoginError] = React.useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const { handleLogin } = useAuthentication();

  const { control, handleSubmit } = useForm({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  });

  const from = location.state?.from?.pathname || '/';

  const onSubmit = async data => {
    try {
      await handleLogin(data?.email, data?.password);

      navigate(from, { replace: true });
    } catch (error) {
      error?.response?.status === 401
        ? setLoginError('Invalid credentials')
        : setLoginError(
            'Something went wrong (Are you connected to the internet)',
          );
    }
  };

  return (
    <Grid container component='main' sx={{ height: '100vh' }}>
      <Grid
        xs={false}
        sm={4}
        md={7}
        sx={{
          backgroundImage: `url(${signInImgUrl})`,
          backgroundRepeat: 'no-repeat',
          backgroundColor: theme =>
            theme.palette.mode === 'light'
              ? theme.palette.grey[50]
              : theme.palette.grey[900],
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      />
      <Grid xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <Box
          sx={{
            my: 8,
            mx: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component='h1' variant='h5'>
            Sign in
          </Typography>
          <Box
            component='form'
            noValidate
            onSubmit={handleSubmit(onSubmit)}
            sx={{
              mt: 1,
              '& .MuiInputBase-root': {
                pt: '8px',
                pb: '8px',
              },
            }}
          >
            <ControlledTextField
              name='email'
              control={control}
              loginError={loginError}
              label='Email Address'
              autoComplete='email'
              autoFocus
            />
            <ControlledTextField
              name='password'
              control={control}
              loginError={loginError}
              label='Password'
              type='password'
              autoComplete='new-password'
            />
            <FormControlLabel
              control={<Checkbox value='remember' color='primary' />}
              label='Remember me'
            />
            <Button
              type='submit'
              fullWidth
              variant='contained'
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid xs>
                <Link href='#' variant='body2'>
                  Forgot password?
                </Link>
              </Grid>
              <Grid>
                <Link variant='body2' onClick={() => navigate('/signup')}>
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
            <AuthFooter sx={{ mt: 5 }} />
          </Box>
        </Box>
      </Grid>
    </Grid>
  );
}
