import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

import ResponsiveIllustration from './ResponsiveIllustration';

export default function NotFound() {
  const navigate = useNavigate();

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
            xs: 'h3',
            sm: 'h1',
          },
        }}
      >
        404 Not Found
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
