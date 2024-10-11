import { styled } from '@mui/system';

import Illustration from '../assets/javascript-illustration.svg?react';

const ResponsiveIllustration = styled(Illustration)(({ theme }) => ({
  width: '400px',
  [theme.breakpoints.down('sm')]: {
    width: '300px',
  },
}));

export default ResponsiveIllustration;
