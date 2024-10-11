import * as React from 'react';
import CloseIcon from '@mui/icons-material/Close';
import ExpandMoreRoundedIcon from '@mui/icons-material/ExpandMoreRounded';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import PropTypes from 'prop-types';

import Info from './Info';

export default function InfoMobile({ cartItems }) {
  const [open, setOpen] = React.useState(false);

  const toggleDrawer = newOpen => () => {
    setOpen(newOpen);
  };

  const DrawerList = (
    <Box sx={{ width: 'auto', px: 3, pb: 3, pt: 8 }} role='presentation'>
      <IconButton
        onClick={toggleDrawer(false)}
        sx={{ position: 'absolute', right: 8, top: 8 }}
      >
        <CloseIcon />
      </IconButton>
      <Info cartItems={cartItems} />
    </Box>
  );

  return (
    <div>
      <Button
        variant='text'
        endIcon={<ExpandMoreRoundedIcon />}
        onClick={toggleDrawer(true)}
      >
        View details
      </Button>
      <Drawer open={open} anchor='top' onClose={toggleDrawer(false)}>
        {DrawerList}
      </Drawer>
    </div>
  );
}

const userPropTypes = PropTypes.shape({
  id: PropTypes.string,
  email: PropTypes.string,
  firstName: PropTypes.string,
  lastName: PropTypes.string,
  role: PropTypes.string,
});

const productPropTypes = PropTypes.shape({
  id: PropTypes.string,
  name: PropTypes.string,
  description: PropTypes.string,
  price: PropTypes.number,
  category: PropTypes.string,
});

const cartItemPropTypes = PropTypes.shape({
  id: PropTypes.string,
  user: userPropTypes,
  product: productPropTypes,
  quantity: PropTypes.number,
});

InfoMobile.propTypes = {
  cartItems: PropTypes.arrayOf(cartItemPropTypes),
};
