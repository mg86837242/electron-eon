import { useNavigate } from 'react-router-dom';
import Link from '@mui/material/Link';
import Typography from '@mui/material/Typography';

export default function Copyright(props) {
  const navigate = useNavigate();

  return (
    <Typography
      variant='body2'
      color='text.secondary'
      align='center'
      {...props}
    >
      {'Copyright © '}
      <Link color='inherit' onClick={() => navigate('/')}>
        Electron&nbsp;Eon
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}
