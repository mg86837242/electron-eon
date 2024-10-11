import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';

export default function Spinner() {
  return (
    <Box
      sx={{
        position: 'fixed',
        top: '40%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        zIndex: 9999, // Ensures it's on the top layer
      }}
    >
      <CircularProgress size={80} thickness={2} />
    </Box>
  );
}
