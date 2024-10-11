import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import PropTypes from 'prop-types';

import ResponsiveIllustration from './ResponsiveIllustration';

export default function Forbidden({ message }) {
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
        403 {message || 'Forbidden'}
      </Typography>
      {message === 'Visit your cart first before checking out please' ? (
        <Button
          variant='contained'
          color='primary'
          size='large'
          onClick={() => navigate('/cart', { replace: true })}
        >
          Go To Cart Page
        </Button>
      ) : (
        <Button
          variant='contained'
          color='primary'
          size='large'
          onClick={() => navigate('/', { replace: true })}
        >
          Back To Landing Page
        </Button>
      )}
    </Box>
  );
}

Forbidden.propTypes = {
  message: PropTypes.string,
};
