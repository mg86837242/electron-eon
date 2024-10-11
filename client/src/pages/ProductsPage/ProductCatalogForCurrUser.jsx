import * as React from 'react';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Unstable_Grid2';
import { useQuery } from '@tanstack/react-query';

import { getAllProdsForCurrUser } from '../../api';

import ProductCard from './ProductCard';

export default function ProductCatalogForCurrUser() {
  const { data: products } = useQuery({
    queryKey: ['getAllProdsForCurrUser'],
    queryFn: getAllProdsForCurrUser,
  });

  return (
    <Box
      sx={{
        flexGrow: 1,
      }}
    >
      <Grid container spacing={{ xs: 2, md: 3 }}>
        {products
          ?.toSorted((a, b) => {
            const aCat = a?.category ?? '';
            const bCat = b?.category ?? '';
            return aCat.localeCompare(bCat);
          })
          ?.map(({ id, name, description, price, category, cartQuantity }) => (
            <Grid xs={12} mds={6} sm={6} md={4} lg={3} key={id}>
              <ProductCard
                id={id}
                name={name}
                description={description}
                price={price}
                category={category}
                cartQuantity={cartQuantity}
              />
            </Grid>
          ))}
      </Grid>
    </Box>
  );
}
