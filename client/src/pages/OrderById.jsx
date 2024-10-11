import * as React from 'react';
import { useParams } from 'react-router-dom';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Typography from '@mui/material/Typography';
import { useQuery } from '@tanstack/react-query';

import { getOrderByIdForCurrUser } from '../api';
import useAuthStore from '../store/useAuthStore';
import capFirst from '../utils/capFirst';

export default function OrderById() {
  const { id } = useParams();
  const orderResponse = useAuthStore(state => state.orderResponse);

  const { data: order } = useQuery({
    queryKey: ['getOrderByIdForCurrUser', id],
    queryFn: () => getOrderByIdForCurrUser(id),
    // Cond initial data from cache: https://tanstack.com/query/latest/docs/framework/react/guides/initial-query-data#conditional-initial-data-from-cache
    /**
     * 1. On First Mount:
     * -- If `initialData` is available, the query will return immediately with the `initialData`, and the data will be
     *    cached for future use.
     * -- If `initialData` is not available, the query data will be fetched from the backend, and the query will return
     *    with the fetched data, which will also be cached for future use.
     * -- The presence of `refetchOnMount`, `refetchOnWindowFocus`, `refetchOnReconnect`, or `refetchInterval` can
     *    affect this behavior:
     *    -- If `refetchOnMount` is `true`, the query will be refetched immediately after the `initialData` is returned,
     *       regardless of whether `initialData` is provided or fetched from the backend.
     *    -- Similarly, if `refetchOnWindowFocus` is `true`, the query will be refetched when the window regains focus.
     *    -- If `refetchOnReconnect` is `true`, the query will be refetched when the network reconnects.
     *    -- If `refetchInterval` is specified, the query will be automatically refetched at the specified interval,
     *       regardless of other conditions.
     * 2. After 10 Minutes {@link queryClient} of First Mount:
     * -- If there is no refreshing (manual or automatic), no refocusing of the window, and no network reconnection,
     *    the query data will remain cached but will be marked as stale after 10 minutes.
     * -- The cached data will still be used for rendering, but if a subsequent query occurs (due to a change in query
     *    key or manual refetch), the cached data may be considered stale, and React Query may trigger a refetch based
     *    on the conditions set for refetching.
     */
    initialData: () => {
      if (orderResponse) {
        return orderResponse;
      }
    },
    // With this `placeholderData`, pending indicator (e.g., spinner) is not need for this page, the indicator
    //  displayed in this screen comes from <RequireAuth>;
    // `placeholderData` vs `initialData`: https://tanstack.com/query/latest/docs/framework/react/community/tkdodos-blog#9-placeholder-and-initial-data-in-react-query
    placeholderData: {
      city: '',
      street: '',
      user: { firstName: '', lastName: '', email: '' },
      orderProducts: [],
      createdAt: '',
    },
  });

  const {
    city,
    street,
    user: { firstName, lastName, email },
    orderProducts,
    createdAt,
  } = order;
  const totalPrice = orderProducts
    ?.reduce((acc, cv) => acc + cv?.product?.price * cv?.quantity, 0)
    ?.toFixed(2);

  return (
    <>
      <Typography variant='subtitle2' color='text.secondary'>
        Order no.: {id}
      </Typography>
      <Typography variant='h4' gutterBottom>
        Order Information
      </Typography>
      <List disablePadding>
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText sx={{ mr: 2 }} primary={'Created at:'} secondary={''} />
          <Typography variant='body1' fontWeight='medium'>
            {new Date(createdAt).toLocaleString('en-US', {
              year: 'numeric',
              month: 'long',
              day: 'numeric',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: true,
            })}
          </Typography>
        </ListItem>
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText sx={{ mr: 2 }} primary={'First name:'} secondary={''} />
          <Typography variant='body1' fontWeight='medium'>
            {capFirst(firstName)}
          </Typography>
        </ListItem>
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText sx={{ mr: 2 }} primary={'Last name:'} secondary={''} />
          <Typography variant='body1' fontWeight='medium'>
            {capFirst(lastName)}
          </Typography>
        </ListItem>
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText sx={{ mr: 2 }} primary={'Email:'} secondary={''} />
          <Typography variant='body1' fontWeight='medium'>
            {email}
          </Typography>
        </ListItem>
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText sx={{ mr: 2 }} primary={'Street:'} secondary={''} />
          <Typography variant='body1' fontWeight='medium'>
            {street}
          </Typography>
        </ListItem>
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText sx={{ mr: 2 }} primary={'City:'} secondary={''} />
          <Typography variant='body1' fontWeight='medium'>
            {city}
          </Typography>
        </ListItem>
        {orderProducts?.map(
          ({ product: { id: productId, name, price }, quantity }) => (
            <ListItem sx={{ py: 1, px: 0 }} key={productId}>
              <ListItemText
                sx={{ mr: 2 }}
                primary={name}
                secondary={`price: $${price}`}
              />
              <Typography variant='body1' fontWeight='medium'>
                {quantity}
              </Typography>
            </ListItem>
          ),
        )}
        <ListItem sx={{ py: 1, px: 0 }}>
          <ListItemText
            sx={{ mr: 2 }}
            primary={'Total price:'}
            secondary={''}
          />
          <Typography variant='body1' fontWeight='medium'>
            $ {totalPrice ?? 0}
          </Typography>
        </ListItem>
      </List>
    </>
  );
}
