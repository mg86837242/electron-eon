import * as React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import PropTypes from 'prop-types';

export default function AppBarDrawerActions({
  isAuthenticated,
  handleLogout,
  toggleDrawer,
}) {
  const navigate = useNavigate();
  const location = useLocation();

  const onLogout = () => {
    toggleDrawer(false);
    navigate('/');
  };

  return isAuthenticated ? (
    <>
      <MenuItem>
        <Button
          color='primary'
          variant='outlined'
          component='a'
          onClick={() => navigate('/cart')}
          sx={{ width: '100%' }}
        >
          Cart
        </Button>
      </MenuItem>
      <MenuItem>
        <Button
          color='primary'
          variant='contained'
          component='a'
          onClick={() => handleLogout(onLogout)}
          sx={{ width: '100%' }}
        >
          Log out
        </Button>
      </MenuItem>
    </>
  ) : (
    <>
      <MenuItem>
        <Button
          color='primary'
          variant='outlined'
          component='a'
          onClick={() => navigate('/signup', { state: { from: location } })}
          sx={{ width: '100%' }}
        >
          Sign up
        </Button>
      </MenuItem>
      <MenuItem>
        <Button
          color='primary'
          variant='contained'
          component='a'
          onClick={() => navigate('/login', { state: { from: location } })}
          sx={{ width: '100%' }}
        >
          Log in
        </Button>
      </MenuItem>
    </>
  );
}

AppBarDrawerActions.propTypes = {
  isAuthenticated: PropTypes.bool.isRequired,
  handleLogout: PropTypes.func.isRequired,
  toggleDrawer: PropTypes.func.isRequired,
};
