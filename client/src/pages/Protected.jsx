import { Box, Typography } from '@mui/material';

import useAuthUser from '../hooks/useAuthUser';

export default function Protected() {
  const { authUser } = useAuthUser();

  return (
    <Box>
      <Typography
        component='h1'
        variant='h1'
        sx={{ mt: { xs: 14, sm: 20 } }} // in consistency with <Hero>'s <Container>
      >
        This is a protected route
      </Typography>
      <Typography component='p' variant='h5' color='success.main' mt={2}>
        Your role is {authUser?.claims?.scope}
      </Typography>
    </Box>
  );
}
