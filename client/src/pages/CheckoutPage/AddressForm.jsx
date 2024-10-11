import * as React from 'react';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormLabel from '@mui/material/FormLabel';
import Grid from '@mui/material/Grid';
import OutlinedInput from '@mui/material/OutlinedInput';
import { styled } from '@mui/system';
import PropTypes from 'prop-types';

import capFirst from '../../utils/capFirst';

const FormGrid = styled(Grid)(() => ({
  display: 'flex',
  flexDirection: 'column',
}));

export default function AddressForm({
  firstName,
  lastName,
  formData,
  setFormData,
}) {
  return (
    <Grid container spacing={3}>
      <FormGrid item xs={12} md={6}>
        <FormLabel htmlFor='first-name' required>
          First name
        </FormLabel>
        <OutlinedInput
          value={capFirst(firstName)}
          id='place-order-first-name'
          name='first-name'
          type='name'
          autoComplete='first name'
          disabled
        />
      </FormGrid>
      <FormGrid item xs={12} md={6}>
        <FormLabel htmlFor='last-name' required>
          Last name
        </FormLabel>
        <OutlinedInput
          value={capFirst(lastName)}
          id='place-order-last-name'
          name='last-name'
          type='last-name'
          autoComplete='last name'
          disabled
        />
      </FormGrid>
      <FormGrid item xs={12}>
        <FormLabel htmlFor='address1' required>
          Address line 1
        </FormLabel>
        <OutlinedInput
          value={formData.street}
          onChange={e =>
            setFormData(prevFormData => ({
              ...prevFormData,
              street: e.target.value,
            }))
          }
          id='place-order-street'
          name='street'
          type='address1'
          placeholder='Street name and number'
          autoComplete='shipping address-line1'
          required
        />
      </FormGrid>
      <FormGrid item xs={12}>
        <FormLabel htmlFor='city' required>
          City
        </FormLabel>
        <OutlinedInput
          value={formData.city}
          onChange={e =>
            setFormData(prevFormData => ({
              ...prevFormData,
              city: e.target.value,
            }))
          }
          id='place-order-city'
          name='city'
          type='city'
          placeholder='New York'
          autoComplete='City'
          required
        />
      </FormGrid>
      <FormGrid item xs={12}>
        <FormControlLabel
          control={<Checkbox name='saveAddress' value='yes' />}
          label='Use this address for payment details'
        />
      </FormGrid>
    </Grid>
  );
}

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

AddressForm.propTypes = {
  firstName: PropTypes.string,
  lastName: PropTypes.string,
  formData: formDataPropTypes,
  setFormData: PropTypes.func,
};
