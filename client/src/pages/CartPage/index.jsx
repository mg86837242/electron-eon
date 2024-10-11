import * as React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import { Box } from '@mui/material';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';

import {
  decrementProdQtyInCartById,
  deleteCartById,
  getCartsForCurrUser,
  incrementProdQtyInCartById,
} from '../../api';

import {
  TableCellBody,
  TableCellBodyRight,
  TableCellHead,
} from './StyledTableCells';

export default function CartTable() {
  const navigate = useNavigate();
  const location = useLocation();

  const { data: cartItems } = useQuery({
    queryKey: ['getCartsForCurrUser'],
    queryFn: getCartsForCurrUser,
  });

  const queryClient = useQueryClient();

  const { mutate: incMutate } = useMutation({
    mutationFn: incrementProdQtyInCartById,
    onMutate: async id => {
      await queryClient.cancelQueries({ queryKey: ['getCartsForCurrUser'] });
      const previousCarts = queryClient.getQueryData(['getCartsForCurrUser']);
      queryClient.setQueryData(['getCartsForCurrUser'], old =>
        old.map(c => (c.id === id ? { ...c, quantity: c.quantity + 1 } : c)),
      );
      return { previousCarts };
    },
    onError: (_error, _newProduct, context) => {
      queryClient.setQueryData(
        ['getAllProdsForCurrUser'],
        context.previousProducts,
      );
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['getAllProdsForCurrUser'] });
    },
  });

  const { mutate: decMutate } = useMutation({
    mutationFn: decrementProdQtyInCartById,
    onMutate: async id => {
      await queryClient.cancelQueries({ queryKey: ['getCartsForCurrUser'] });
      const previousCarts = queryClient.getQueryData(['getCartsForCurrUser']);
      queryClient.setQueryData(['getCartsForCurrUser'], old =>
        old.reduce(
          (acc, c) =>
            c.id === id
              ? c.quantity - 1 <= 0
                ? acc
                : [...acc, { ...c, quantity: c.quantity - 1 }]
              : [...acc, c],
          [],
        ),
      );
      return { previousCarts };
    },
    onError: (_error, _newProduct, context) => {
      queryClient.setQueryData(
        ['getAllProdsForCurrUser'],
        context.previousProducts,
      );
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['getAllProdsForCurrUser'] });
    },
  });

  const { mutate: delMutate } = useMutation({
    mutationFn: deleteCartById,
    onMutate: async id => {
      await queryClient.cancelQueries({ queryKey: ['getCartsForCurrUser'] });
      const previousCarts = queryClient.getQueryData(['getCartsForCurrUser']);
      queryClient.setQueryData(['getCartsForCurrUser'], old =>
        old.filter(c => c.id !== id),
      );
      return { previousCarts };
    },
    onError: (_error, _id, context) => {
      queryClient.setQueryData(['getCartsForCurrUser'], context.previousCarts);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['getCartsForCurrUser'] });
    },
  });

  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label='shopping cart table'>
          <TableHead sx={{ backgroundColor: 'primary.main' }}>
            <TableRow>
              <TableCellHead>Product Name</TableCellHead>
              <TableCellHead align='right'>Price</TableCellHead>
              <TableCellHead align='right'>Quantity</TableCellHead>
              <TableCellHead align='right'>Delete</TableCellHead>
            </TableRow>
          </TableHead>
          <TableBody>
            {cartItems
              ?.toSorted((a, b) => {
                const aName = a.product?.name ?? '';
                const bName = b.product?.name ?? '';
                return aName.localeCompare(bName);
              })
              ?.map(({ id, product: { name, price }, quantity }) => (
                <TableRow
                  key={id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCellBody component='th' scope='row'>
                    {name}
                  </TableCellBody>
                  <TableCellBodyRight>$ {price}</TableCellBodyRight>
                  <TableCellBodyRight>
                    <Box
                      sx={{
                        display: 'flex',
                        justifyContent: 'end',
                        alignItems: 'center',
                        gap: 1,
                      }}
                    >
                      <Button
                        size='small'
                        aria-label='button to remove item from the shopping cart'
                        sx={{ minWidth: '32px', height: '32px', p: '4px' }}
                        onClick={() => decMutate(id)}
                      >
                        <RemoveCircleRoundedIcon fontSize='medium' />
                      </Button>
                      <Typography
                        variant='body2'
                        color='text.primary'
                        width={20}
                        align='center'
                      >
                        {quantity}
                      </Typography>
                      <Button
                        size='small'
                        aria-label='button to add item from the shopping cart'
                        sx={{ minWidth: '32px', height: '32px', p: '4px' }}
                        onClick={() => incMutate(id)}
                      >
                        <AddCircleRoundedIcon fontSize='medium' />
                      </Button>
                    </Box>
                  </TableCellBodyRight>
                  <TableCellBodyRight>
                    <Button
                      size='small'
                      aria-label='button to delete all entries associated with an item from the shopping cart'
                      sx={{ minWidth: '32px', height: '32px', p: '4px' }}
                      onClick={() => delMutate(id)}
                    >
                      <DeleteForeverIcon fontSize='medium' />
                    </Button>
                  </TableCellBodyRight>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Button
        fullWidth
        variant='contained'
        aria-label='button to proceed to the checkout page'
        sx={{ mt: 3, mb: 2 }}
        disabled={!cartItems?.length}
        onClick={() => navigate('/checkout', { state: { from: location } })}
      >
        Checkout
      </Button>
    </>
  );
}
