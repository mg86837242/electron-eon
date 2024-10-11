import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import PropTypes from 'prop-types';

export default function Info({ cartItems }) {
  const totalPrice = cartItems
    ?.reduce((acc, cv) => acc + cv?.product?.price * cv?.quantity, 0)
    ?.toFixed(2);

  return (
    <>
      <Typography variant='subtitle2' color='text.secondary' align='right'>
        Total
      </Typography>
      <Typography variant='h4' align='right' gutterBottom>
        $ {totalPrice ?? 0}
      </Typography>
      <TableContainer component={Paper}>
        <Table sx={{}} aria-label='simple table'>
          <TableHead>
            <TableRow>
              <TableCell>Product</TableCell>
              <TableCell align='right'>Quantity</TableCell>
              <TableCell align='right'>Subtotal</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {cartItems?.map(
              ({ product: { id: productId, name, price }, quantity }) => (
                <TableRow
                  key={productId}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell component='th' scope='row'>
                    {name}
                  </TableCell>
                  <TableCell align='right'>{quantity}</TableCell>
                  <TableCell align='right'>{quantity * price}</TableCell>
                </TableRow>
              ),
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </>
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

Info.propTypes = {
  cartItems: PropTypes.arrayOf(cartItemPropTypes),
};
