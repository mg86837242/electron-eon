import { useNavigate, useRouteError } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

import ResponsiveIllustration from './ResponsiveIllustration';
export default function DefaultErrorBoundary() {
  const navigate = useNavigate();
  const error = useRouteError();

  return (
    <Box
      height='100vh'
      display='flex'
      flexDirection='column'
      justifyContent='center'
      alignItems='center'
      gap={8}
    >
      <ResponsiveIllustration />
      <Typography
        component='h1'
        color='error.main'
        align='center'
        sx={{
          typography: {
            xs: 'h5',
            sm: 'h3',
          },
        }}
      >
        {error?.message || 'Unknown Error Occurred'}
      </Typography>
      <Button
        variant='contained'
        color='primary'
        size='large'
        onClick={() => navigate('/', { replace: true })}
      >
        Back To Landing Page
      </Button>
    </Box>
  );
}
