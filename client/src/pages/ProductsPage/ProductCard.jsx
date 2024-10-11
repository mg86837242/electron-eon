import * as React from 'react';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActionArea from '@mui/material/CardActionArea';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import PropTypes from 'prop-types';

import {
  decrementProdQtyInCartForCurrUser,
  incrementProdQtyInCartForCurrUser,
} from '../../api';
import cardImgUrl from '../../assets/product-01.jpg';

import {
  TwoLineEllipsisBody2,
  TwoLineEllipsisH5,
} from './TwoLineEllipsisTexts';

export default function ProductCard({
  id,
  name,
  description,
  price,
  category,
  cartQuantity,
}) {
  const queryClient = useQueryClient();

  const { mutate: decMutate } = useMutation({
    mutationFn: decrementProdQtyInCartForCurrUser,
    onMutate: async productId => {
      await queryClient.cancelQueries({ queryKey: ['getAllProdsForCurrUser'] });
      const previousProducts = queryClient.getQueryData([
        'getAllProdsForCurrUser',
      ]);
      const newProduct = {
        id: productId,
        name,
        description,
        price,
        category,
        cartQuantity: Math.max(0, cartQuantity - 1),
      };
      queryClient.setQueryData(['getAllProdsForCurrUser'], old =>
        old.map(p => (p.id === productId ? newProduct : p)),
      );
      return { previousProducts };
    },
    onError: (_error, _productId, context) => {
      queryClient.setQueryData(
        ['getAllProdsForCurrUser'],
        context.previousProducts,
      );
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['getAllProdsForCurrUser'] });
    },
  });

  const { mutate: incMutate } = useMutation({
    mutationFn: incrementProdQtyInCartForCurrUser,
    onMutate: async productId => {
      await queryClient.cancelQueries({ queryKey: ['getAllProdsForCurrUser'] });
      const previousProducts = queryClient.getQueryData([
        'getAllProdsForCurrUser',
      ]);
      const newProduct = {
        id: productId,
        name,
        description,
        price,
        category,
        cartQuantity: cartQuantity + 1,
      };
      queryClient.setQueryData(['getAllProdsForCurrUser'], old =>
        old.map(p => (p.id === productId ? newProduct : p)),
      );
      return { previousProducts };
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

  return (
    <Card sx={{ maxWidth: 345 }}>
      <CardActionArea>
        <CardMedia
          component='img'
          height='320px'
          image={cardImgUrl}
          alt='green iguana'
        />
        <CardContent>
          <TwoLineEllipsisH5 gutterBottom variant='h5' component='div'>
            {name}
          </TwoLineEllipsisH5>
          <TwoLineEllipsisBody2 variant='body2' color='text.secondary'>
            {description}
          </TwoLineEllipsisBody2>
          <Typography variant='body2' color='text.secondary'>
            {category.replaceAll('_', ' ')}
          </Typography>
          <Typography variant='body2' color='text.secondary' align='right'>
            $ {price}
          </Typography>
        </CardContent>
      </CardActionArea>
      {cartQuantity !== null && cartQuantity !== undefined && (
        <CardActions
          sx={{ display: 'flex', justifyContent: 'center', pt: '0' }}
        >
          <Button
            size='small'
            aria-label='button to remove item from the shopping cart'
            sx={{ minWidth: '32px', height: '32px', p: '4px' }}
            onClick={() => decMutate(id)}
          >
            <RemoveCircleRoundedIcon fontSize='medium' />
          </Button>
          <Typography variant='body2' color='text.primary'>
            {cartQuantity}
          </Typography>
          <Button
            size='small'
            aria-label='button to add item from the shopping cart'
            sx={{ minWidth: '32px', height: '32px', p: '4px' }}
            onClick={() => incMutate(id)}
          >
            <AddCircleRoundedIcon fontSize='medium' />
          </Button>
        </CardActions>
      )}
    </Card>
  );
}

ProductCard.propTypes = {
  id: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  price: PropTypes.number.isRequired,
  category: PropTypes.oneOf(['LAPTOP', 'SMARTPHONE', 'COMPUTER_ACCESSORY'])
    .isRequired,
  cartQuantity: PropTypes.number,
};
