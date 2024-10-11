import * as React from 'react';
import ModeNightRoundedIcon from '@mui/icons-material/ModeNightRounded';
import WbSunnyRoundedIcon from '@mui/icons-material/WbSunnyRounded';
import IconButton from '@mui/material/IconButton';
import PropTypes from 'prop-types';

function ToggleColorMode({ mode, toggleColorMode }) {
  return (
    <IconButton
      onClick={toggleColorMode}
      color='primary'
      aria-label='button to toggle theme'
    >
      {mode === 'dark' ? (
        <WbSunnyRoundedIcon fontSize='small' />
      ) : (
        <ModeNightRoundedIcon fontSize='small' />
      )}
    </IconButton>
  );
}

ToggleColorMode.propTypes = {
  mode: PropTypes.oneOf(['dark', 'light']).isRequired,
  toggleColorMode: PropTypes.func.isRequired,
};

export default ToggleColorMode;
