import * as React from 'react';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import PropTypes from 'prop-types';

import capFirst from '../../utils/capFirst';

const payments = [
  { name: 'Card type:', detail: 'Visa' },
  { name: 'Card holder:', detail: 'John Doe' },
  { name: 'Card number:', detail: 'xxxx-xxxx-xxxx-1234' },
  { name: 'Expiry date:', detail: '04/2024' },
];

export default function Review({ cartItems, formData }) {
  const totalPrice = cartItems
    ?.reduce((acc, cv) => acc + cv?.product?.price * cv?.quantity, 0)
    ?.toFixed(2);

  return (
    <Stack spacing={2}>
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
      <Typography
        variant='subtitle2'
        color='text.secondary'
        align='right'
        paddingX={2}
      >
        Total
      </Typography>
      <Typography variant='h4' align='right' paddingX={2} gutterBottom>
        $ {totalPrice ?? 0}
      </Typography>
      <Divider />
      <Stack
        direction='column'
        divider={<Divider flexItem />}
        spacing={2}
        sx={{ my: 2 }}
      >
        <div>
          <Typography variant='subtitle2' gutterBottom>
            Shipment details
          </Typography>
          <Typography gutterBottom>
            {capFirst(cartItems[0]?.user?.firstName)}{' '}
            {capFirst(cartItems[0]?.user?.lastName)}
          </Typography>
          <Typography color='text.secondary' gutterBottom>
            {[formData.street, formData.city].join(', ')}
          </Typography>
        </div>
        <div>
          <Typography variant='subtitle2' gutterBottom>
            Payment details
          </Typography>
          <Grid container>
            {payments.map(payment => (
              <React.Fragment key={payment.name}>
                <Stack
                  direction='row'
                  spacing={1}
                  useFlexGap
                  sx={{ width: '100%', mb: 1 }}
                >
                  <Typography variant='body1' color='text.secondary'>
                    {payment.name}
                  </Typography>
                  <Typography variant='body2'>{payment.detail}</Typography>
                </Stack>
              </React.Fragment>
            ))}
          </Grid>
        </div>
      </Stack>
    </Stack>
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

const formDataPropTypes = PropTypes.shape({
  street: PropTypes.string,
  city: PropTypes.string,
  orderProducts: PropTypes.arrayOf(
    PropTypes.shape({
      productId: PropTypes.string,
      quantity: PropTypes.number,
    }),
  ),
});

Review.propTypes = {
  cartItems: PropTypes.arrayOf(cartItemPropTypes),
  formData: formDataPropTypes,
};
