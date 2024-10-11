import * as React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import PropTypes from 'prop-types';

export default function AppBarRightActions({ isAuthenticated, handleLogout }) {
  const navigate = useNavigate();
  const location = useLocation();

  return isAuthenticated ? (
    <>
      <Box sx={{ maxWidth: '32px' }}>
        <Button
          size='small'
          aria-label='button to toggle theme'
          sx={{ minWidth: '32px', height: '32px', p: '4px' }}
          onClick={() => navigate('/cart')}
        >
          <ShoppingCartIcon fontSize='small' />
        </Button>
      </Box>
      <Button
        color='primary'
        variant='contained'
        size='small'
        component='a'
        onClick={() => handleLogout(() => navigate('/'))}
      >
        Log out
      </Button>
    </>
  ) : (
    <>
      <Button
        color='primary'
        variant='text'
        size='small'
        component='a'
        onClick={() => navigate('/signup', { state: { from: location } })}
      >
        Sign up
      </Button>
      <Button
        color='primary'
        variant='contained'
        size='small'
        component='a'
        onClick={() => navigate('/login', { state: { from: location } })}
      >
        Log in
      </Button>
    </>
  );
}

AppBarRightActions.propTypes = {
  isAuthenticated: PropTypes.bool.isRequired,
  handleLogout: PropTypes.func.isRequired,
};
