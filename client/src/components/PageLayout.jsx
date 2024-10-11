import { Outlet } from 'react-router-dom';
import Container from '@mui/material/Container';

import AppAppBar from './AppAppBar';
import Footer from './Footer';

export default function PageLayout() {
  return (
    <>
      <AppAppBar />
      <Container
        sx={{
          // 100vh - top element's margin if applicable - <Footer>'s height
          minHeight: `calc(100vh - 512px)`,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          pt: { xs: 14, sm: 20 }, // in consistency with <Hero>'s <Container>
          pb: { xs: 8, sm: 12 },
        }}
      >
        <Outlet />
      </Container>
      <Footer />
    </>
  );
}
