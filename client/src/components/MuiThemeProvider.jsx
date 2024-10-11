import * as React from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import PropTypes from 'prop-types';

import getLandingPageTheme from '../pages/LandingPage/getLandingPageTheme';
import useThemeStore from '../store/useThemeStore';

export default function MuiThemeProvider({ children }) {
  const colorMode = useThemeStore(state => state.colorMode);

  const landingPageTheme = React.useMemo(
    () => createTheme(getLandingPageTheme(colorMode)),
    [colorMode],
  );

  return (
    <ThemeProvider theme={landingPageTheme}>
      <CssBaseline />
      {children}
    </ThemeProvider>
  );
}

MuiThemeProvider.propTypes = {
  children: PropTypes.node,
};
