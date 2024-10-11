import * as React from 'react';
import { useController, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { zodResolver } from '@hookform/resolvers/zod';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import LoadingButton from '@mui/lab/LoadingButton';
import Alert from '@mui/material/Alert';
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Checkbox from '@mui/material/Checkbox';
import Container from '@mui/material/Container';
import FormControlLabel from '@mui/material/FormControlLabel';
import Link from '@mui/material/Link';
import Snackbar from '@mui/material/Snackbar';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Unstable_Grid2';
import { useMutation } from '@tanstack/react-query';
import PropTypes from 'prop-types';

import { registerUser } from '../api';
import { AuthFooter } from '../components';
import registerSchema from '../schemas/registerSchema';

function ControlledTextField({
  name,
  control,
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
      error={!!error}
      helperText={error?.message || ''}
      required
      fullWidth
      id={`register-${name}`}
      label={label}
      autoComplete={autoComplete}
      type={type}
      autoFocus={autoFocus}
      variant='filled'
    />
  );
}

ControlledTextField.propTypes = {
  name: PropTypes.string.isRequired,
  control: PropTypes.object.isRequired,
  label: PropTypes.string.isRequired,
  autoComplete: PropTypes.string,
  type: PropTypes.string,
  autoFocus: PropTypes.bool,
};

export default function SignUp() {
  const [isLoading, setIsLoading] = React.useState(false); // custom pending state for fake delay (`setTimeout`)
  const [open, setOpen] = React.useState(false);
  const navigate = useNavigate();

  const { control, handleSubmit } = useForm({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      password: '',
    },
    mode: 'onChange',
  });

  const {
    mutate,
    error: registerError,
    isError,
  } = useMutation({
    mutationFn: registerUser,
    onMutate: () => {
      setIsLoading(true);
    },
    onSuccess: () => {
      setOpen(true);

      setTimeout(() => {
        setIsLoading(false);
        navigate('/', { replace: true });
      }, 5_000);
    },
    onError: () => {
      setIsLoading(false);
    },
  });

  const onSubmit = async data => {
    mutate(data);
  };

  const handleClose = (_event, reason) => {
    if (reason === 'clickaway') {
      return;
    }

    setOpen(false);
  };

  return (
    <Container component='main' maxWidth='xs'>
    <Box
      sx={{
        mt: 8,
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
      }}
    >
      <Avatar sx={{ m: 1, bgcolor: 'primary.main' }}>
        <LockOutlinedIcon />
      </Avatar>
      <Typography component='h1' variant='h5'>
        Sign up
      </Typography>
      <Box
        component='form'
        noValidate
        onSubmit={handleSubmit(onSubmit)}
        sx={{
          mt: 3,
          '& .MuiTextField-root': {
            mb: '8px',
          },
          '& .MuiInputBase-root': {
            pt: '8px',
            pb: '8px',
          },
        }}
      >
        <Grid container spacing={2} justifyContent='center'>
          <Grid item xs={12} sm={6}>
            <ControlledTextField
              name='firstName'
              control={control}
              label='First Name'
              autoComplete='given-name'
              autoFocus
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <ControlledTextField
              name='lastName'
              control={control}
              label='Last Name'
              autoComplete='family-name'
            />
          </Grid>
          <Grid item xs={12}>
            <ControlledTextField
              name='email'
              control={control}
              label='Email Address'
              autoComplete='email'
            />
          </Grid>
          <Grid item xs={12}>
            <ControlledTextField
              name='password'
              control={control}
              label='Password'
              type='password'
              autoComplete='new-password'
            />
          </Grid>
          <Grid xs={12}>
            <FormControlLabel
              sx={{
                mr: 0,
                // ??? For some reason, the following customization doesn't work, despite aligning with: https://mui.com/material-ui/customization/how-to-customize/#overriding-nested-component-styles
                // '.MuiGrid-root > .MuiFormControlLabel-root': {
                //   marginRight: 0,
                // },
              }}
              control={<Checkbox value='allowExtraEmails' color='primary' />}
              label='I want to receive inspiration, marketing promotions and updates via email.'
            />
          </Grid>
          {isError && (
            <Grid xs={12} display='flex' justifyContent='center'>
              <Typography component='p' variant='body1' color='error.main'>
                {registerError?.response?.data?.message ||
                  'An error occurred'}
              </Typography>
            </Grid>
          )}
        </Grid>
        <LoadingButton
          type='submit'
          fullWidth
          variant='contained'
          sx={{ mt: 3, mb: 2 }}
          loading={isLoading}
          loadingIndicator='Pending...'
        >
          Sign Up
        </LoadingButton>
        <Snackbar
          anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
          open={open}
          autoHideDuration={5_000}
          onClose={handleClose}
        >
          <Alert
            onClose={handleClose}
            severity='success'
            variant='filled'
            sx={{ width: '100%' }}
          >
            Registration successful
          </Alert>
        </Snackbar>
        <Grid container justifyContent='flex-end'>
          <Grid>
            <Link
              variant='body2'
              // After redirecting to the login page, successful login will navigate the user to the landing page
              onClick={() => navigate('/login')}
            >
              Already have an account? Sign in
            </Link>
          </Grid>
        </Grid>
      </Box>
    </Box>
    <AuthFooter sx={{ mt: 5 }} />
  </Container>
  );
}

// Reference:
// -- email regex pattern: https://stackoverflow.com/questions/201323/ => linter error => use schema validation
// -- need validation on change => `useForm`'s `mode` option and `register` since it actually works w/ MUI:
//    https://www.youtube.com/watch?v=sD9fZxMO1us => not recommended by MUI docs nor RHF docs
// -- need validation on change with <Controller> or `useController => use `trigger` returned by `useForm`:
//    https://old.reddit.com/r/reactjs/comments/18lcv5a/ => use `trigger`returned by `useForm`, then pass down `trigger`
//    and add `trigger(${name})` to each <TextField> => updating one field causes all fields to be re-rendered as well,
//    despite using `trigger(${name})` according to the docs: https://react-hook-form.com/docs/useform/trigger =>
//    https://github.com/react-hook-form/react-hook-form/issues/1108: incl. `mode: 'onChange'` option for `useForm`,
//    then pass down the returned `setValue` and add `onChange={ e => { inputProps.onChange(e); setValue(name, e.target.
//    value, { shouldValidate: true }); }}` to the `onChange` prop of each <TextField> => solve the issue => somehow,
//    the validation on change still works as intended without re-rendering all fields after removing anything related
//    to `setValue`
