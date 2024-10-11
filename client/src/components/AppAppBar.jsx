import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import MenuIcon from '@mui/icons-material/Menu';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import Divider from '@mui/material/Divider';
import Drawer from '@mui/material/Drawer';
import MenuItem from '@mui/material/MenuItem';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

import logo from '../assets/logo-01.svg';
import useAuthentication from '../hooks/useAuthentication';
import useAuthUser from '../hooks/useAuthUser';
import useThemeStore from '../store/useThemeStore';

import AppBarDrawerActions from './AppBarDrawerActions';
import AppBarRightActions from './AppBarRightActions';
import AppBarToggleColorMode from './AppBarToggleColorMode';

const logoStyle = {
  width: '140px',
  height: 'auto',
  cursor: 'pointer',
};

export default function AppAppBar() {
  const [open, setOpen] = React.useState(false);
  const navigate = useNavigate();
  const colorMode = useThemeStore(state => state.colorMode);
  const toggleColorMode = useThemeStore(state => state.toggleColorMode);
  const { authUser } = useAuthUser(); // TODO highly likely need the pending state to handle the case when the `authUser` is not populated, but since cache is being used, it's not urgent to fix this
  const { handleLogout } = useAuthentication();

  const isAuthenticated = !!authUser;

  const toggleDrawer = newOpen => () => {
    setOpen(newOpen);
  };

  const scrollToSection = sectionId => {
    const sectionElement = document.getElementById(sectionId);
    const offset = 128;
    if (sectionElement) {
      const targetScroll = sectionElement.offsetTop - offset;
      sectionElement.scrollIntoView({ behavior: 'smooth' });
      window.scrollTo({
        top: targetScroll,
        behavior: 'smooth',
      });
      setOpen(false);
    }
  };

  return (
    <div>
      <AppBar
        position='fixed'
        sx={{
          boxShadow: 0,
          bgcolor: 'transparent',
          backgroundImage: 'none',
          mt: 2,
        }}
      >
        <Container maxWidth='lg'>
          <Toolbar
            variant='regular'
            sx={theme => ({
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              flexShrink: 0,
              borderRadius: '999px',
              bgcolor:
                theme.palette.mode === 'light'
                  ? 'rgba(255, 255, 255, 0.4)'
                  : 'rgba(0, 0, 0, 0.4)',
              backdropFilter: 'blur(24px)',
              maxHeight: 40,
              border: '1px solid',
              borderColor: 'divider',
              boxShadow:
                theme.palette.mode === 'light'
                  ? // brand[300]
                    `0 0 1px rgba(255, 181, 76, 0.1), 1px 1.5px 2px -1px rgba(255, 181, 76, 0.15), 4px 4px 12px -2.5px rgba(255, 181, 76, 0.15)`
                  : // brand[900]
                    '0 0 1px rgba(255, 75, 4, 0.7), 1px 1.5px 2px -1px rgba(234, 75, 4, 0.65), 4px 4px 12px -2.5px rgba(234, 75, 4, 0.65)',
            })}
          >
            <Box
              sx={{
                flexGrow: 1,
                display: 'flex',
                alignItems: 'center',
                px: 0,
              }}
            >
              <img
                src={logo}
                style={logoStyle}
                alt='Logo of the website'
                onClick={() => navigate('/')}
              />
              <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
                <MenuItem
                  onClick={() => navigate('/')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Home
                  </Typography>
                </MenuItem>
                <MenuItem
                  onClick={() => navigate('products')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Products
                  </Typography>
                </MenuItem>
                <MenuItem
                  onClick={() => navigate('protected')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Protected
                  </Typography>
                </MenuItem>
                {/* <MenuItem
                  onClick={() => scrollToSection('features')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Features
                  </Typography>
                </MenuItem>
                <MenuItem
                  onClick={() => scrollToSection('testimonials')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Testimonials
                  </Typography>
                </MenuItem>
                <MenuItem
                  onClick={() => scrollToSection('highlights')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Highlights
                  </Typography>
                </MenuItem>
                <MenuItem
                  onClick={() => scrollToSection('pricing')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    Pricing
                  </Typography>
                </MenuItem>
                <MenuItem
                  onClick={() => scrollToSection('faq')}
                  sx={{ py: '6px', px: '12px' }}
                >
                  <Typography variant='body2' color='text.primary'>
                    FAQ
                  </Typography>
                </MenuItem> */}
              </Box>
            </Box>
            <Box
              sx={{
                display: { xs: 'none', md: 'flex' },
                gap: 1,
                alignItems: 'center',
              }}
            >
              <AppBarToggleColorMode
                mode={colorMode}
                toggleColorMode={toggleColorMode}
              />
              <AppBarRightActions
                isAuthenticated={isAuthenticated}
                handleLogout={handleLogout}
              />
            </Box>
            <Box sx={{ display: { sm: '', md: 'none' } }}>
              <Button
                variant='text'
                color='primary'
                aria-label='menu'
                onClick={toggleDrawer(true)}
                sx={{ minWidth: '30px', p: '4px' }}
              >
                <MenuIcon />
              </Button>
              <Drawer anchor='right' open={open} onClose={toggleDrawer(false)}>
                <Box
                  sx={{
                    minWidth: '60dvw',
                    p: 2,
                    backgroundColor: 'background.paper',
                    flexGrow: 1,
                  }}
                >
                  <Box
                    sx={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'end',
                      flexGrow: 1,
                    }}
                  >
                    <AppBarToggleColorMode
                      mode={colorMode}
                      toggleColorMode={toggleColorMode}
                    />
                  </Box>
                  <MenuItem onClick={() => navigate('/')}>Home</MenuItem>
                  <MenuItem onClick={() => navigate('products')}>
                    Products
                  </MenuItem>
                  <MenuItem onClick={() => navigate('protected')}>
                    Protected
                  </MenuItem>
                  {/* <MenuItem onClick={() => scrollToSection('features')}>
                    Features
                  </MenuItem>
                  <MenuItem onClick={() => scrollToSection('testimonials')}>
                    Testimonials
                  </MenuItem>
                  <MenuItem onClick={() => scrollToSection('highlights')}>
                    Highlights
                  </MenuItem>
                  <MenuItem onClick={() => scrollToSection('pricing')}>
                    Pricing
                  </MenuItem>
                  <MenuItem onClick={() => scrollToSection('faq')}>
                    FAQ
                  </MenuItem> */}
                  <Divider />
                  <AppBarDrawerActions
                    isAuthenticated={isAuthenticated}
                    handleLogout={handleLogout}
                    toggleDrawer={toggleDrawer}
                  />
                </Box>
              </Drawer>
            </Box>
          </Toolbar>
        </Container>
      </AppBar>
    </div>
  );
}
