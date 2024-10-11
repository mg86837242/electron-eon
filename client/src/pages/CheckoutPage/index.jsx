import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import ArrowBackRoundedIcon from '@mui/icons-material/ArrowBackRounded';
import ChevronLeftRoundedIcon from '@mui/icons-material/ChevronLeftRounded';
import ChevronRightRoundedIcon from '@mui/icons-material/ChevronRightRounded';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import Stepper from '@mui/material/Stepper';
import Typography from '@mui/material/Typography';
import { useMutation, useQuery } from '@tanstack/react-query';

import { addOrderForCurrUser, getCartsForCurrUser } from '../../api';
import logo from '../../assets/logo-01.svg';
import useAuthStore from '../../store/useAuthStore';
import useThemeStore from '../../store/useThemeStore';

import AddressForm from './AddressForm';
import Info from './Info';
import InfoMobile from './InfoMobile';
import PaymentForm from './PaymentForm';
import Review from './Review';
import ToggleColorMode from './ToggleColorMode';

const steps = ['Shipping address', 'Payment details', 'Review your order'];

const logoStyle = {
  width: '140px',
  height: '56px',
  marginLeft: '8px',
};

export default function CheckoutPage() {
  const [activeStep, setActiveStep] = React.useState(0);
  const [orderResponseId, setOrderResponseId] = React.useState('');
  const navigate = useNavigate();
  const colorMode = useThemeStore(state => state.colorMode);
  const toggleColorMode = useThemeStore(state => state.toggleColorMode);
  const updateHasVisitedCheckout = useAuthStore(
    state => state.updateHasVisitedCheckout,
  );
  const updateOrderResponse = useAuthStore(state => state.updateOrderResponse);

  const { data: cartItems } = useQuery({
    queryKey: ['getCartsForCurrUser'],
    queryFn: getCartsForCurrUser,
    placeholderData: [
      {
        id: '',
        user: {
          id: '',
          email: '',
          firstName: '',
          lastName: '',
          role: '',
        },
        product: {
          id: '',
          name: '',
          description: '',
          price: 0,
          category: '',
        },
        quantity: 0,
      },
    ],
  });

  const orderProductsInCart = cartItems?.map(cartItem => ({
    productId: cartItem?.product?.id,
    quantity: cartItem?.quantity,
  }));

  const [formData, setFormData] = React.useState({
    street: '',
    city: '',
    orderProducts: orderProductsInCart,
  });

  const { mutate } = useMutation({
    mutationFn: addOrderForCurrUser,
    onError: error => {
      console.error('Error occurred:', error);
    },
    onSuccess: data => {
      setOrderResponseId(data.id);
      updateOrderResponse(data);
    },
  });

  const handleNext = () => {
    if (activeStep === 2) {
      mutate(formData);
      updateHasVisitedCheckout(false);
    }

    setActiveStep(activeStep + 1);
  };

  const handleBack = () => {
    setActiveStep(activeStep - 1);
  };

  function getStepContent(step) {
    switch (step) {
      case 0:
        return (
          <AddressForm
            firstName={cartItems[0]?.user?.firstName}
            lastName={cartItems[0]?.user?.lastName}
            formData={formData}
            setFormData={setFormData}
          />
        );
      case 1:
        return <PaymentForm />;
      case 2:
        return <Review cartItems={cartItems} formData={formData} />;
      default:
        throw new Error('Unknown step');
    }
  }

  function getPrevBtnAriaLabel(step) {
    switch (step) {
      case 1:
        return 'button to go to the previous step of checkout, which involves filling in name and address details';
      case 2:
        return 'button to go to the previous step of checkout, which involves filling in payment details';
      default:
        throw new Error('Unknown step');
    }
  }

  function getNextBtnAriaLabel(step) {
    switch (step) {
      case 0:
        return 'button to go to the next step of checkout, which involves filling in payment details';
      case 1:
        return 'button to go to the next step of checkout, which involves reviewing all order details';
      case 2:
        return 'button to submit and place the order';
      default:
        throw new Error('Unknown step');
    }
  }

  return (
    <>
      <Grid container sx={{ height: { xs: '100%', sm: '100dvh' } }}>
        <Grid
          item
          xs={12}
          sm={5}
          lg={4}
          sx={{
            display: { xs: 'none', md: 'flex' },
            flexDirection: 'column',
            backgroundColor: 'background.paper',
            borderRight: { sm: 'none', md: '1px solid' },
            borderColor: { sm: 'none', md: 'divider' },
            alignItems: 'start',
            pt: 4,
            px: 10,
            gap: 4,
          }}
        >
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'end',
              alignItems: 'end',
              height: 150,
              width: '100%',
            }}
          >
            <ToggleColorMode
              mode={colorMode}
              toggleColorMode={toggleColorMode}
            />
            <Button
              startIcon={<ArrowBackRoundedIcon />}
              component='a'
              sx={{ ml: -1 }}
              onClick={() => navigate('/cart')}
            >
              Back to
              <img src={logo} style={logoStyle} alt='Logo of the website' />
            </Button>
          </Box>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              flexGrow: 1,
              width: '100%',
              maxWidth: 500,
            }}
          >
            <Info cartItems={cartItems} />
          </Box>
        </Grid>
        <Grid
          item
          sm={12}
          md={7}
          lg={8}
          sx={{
            display: 'flex',
            flexDirection: 'column',
            maxWidth: '100%',
            width: '100%',
            backgroundColor: { xs: 'transparent', sm: 'background.default' },
            alignItems: 'start',
            pt: { xs: 2, sm: 4 },
            px: { xs: 2, sm: 10 },
            gap: { xs: 4, md: 6 },
          }}
        >
          <Box
            sx={{
              display: 'flex',
              justifyContent: { sm: 'space-between', md: 'flex-end' },
              alignItems: 'center',
              width: '100%',
              maxWidth: { sm: '100%', md: 600 },
            }}
          >
            <Box
              sx={{
                display: { xs: 'flex', md: 'none' },
                flexDirection: 'row',
                width: '100%',
                justifyContent: 'space-between',
              }}
            >
              <Button
                startIcon={<ArrowBackRoundedIcon />}
                component='a'
                sx={{ alignSelf: 'start' }}
                onClick={() => navigate('/cart')}
              >
                Back to
                <img src={logo} style={logoStyle} alt='Logo of the website' />
              </Button>
              <ToggleColorMode
                mode={colorMode}
                toggleColorMode={toggleColorMode}
              />
            </Box>
            <Box
              sx={{
                display: { xs: 'none', md: 'flex' },
                flexDirection: 'column',
                justifyContent: 'space-around',
                alignItems: 'flex-end',
                flexGrow: 1,
                height: 100,
              }}
            >
              <Stepper
                id='desktop-stepper'
                activeStep={activeStep}
                sx={{
                  width: '100%',
                  height: 40,
                }}
              >
                {steps.map(label => (
                  <Step
                    sx={{
                      ':first-of-type': { pl: 0 },
                      ':last-child': { pr: 0 },
                    }}
                    key={label}
                  >
                    <StepLabel>{label}</StepLabel>
                  </Step>
                ))}
              </Stepper>
            </Box>
          </Box>
          <Card
            sx={{
              display: { xs: 'flex', md: 'none' },
              width: '100%',
            }}
          >
            <CardContent
              sx={{
                display: 'flex',
                width: '100%',
                alignItems: 'center',
                justifyContent: 'space-between',
                ':last-child': { pb: 2 },
              }}
            >
              <div>
                <Typography variant='subtitle2' gutterBottom>
                  Selected products
                </Typography>
                <Typography variant='body1'>
                  {activeStep >= 2 ? '$144.97' : '$134.98'}
                </Typography>
              </div>
              <InfoMobile cartItems={cartItems} />
            </CardContent>
          </Card>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              flexGrow: 1,
              width: '100%',
              maxWidth: { sm: '100%', md: 600 },
              maxHeight: '720px',
              gap: { xs: 5, md: 'none' },
            }}
          >
            <Stepper
              id='mobile-stepper'
              activeStep={activeStep}
              alternativeLabel
              sx={{ display: { sm: 'flex', md: 'none' } }}
            >
              {steps.map(label => (
                <Step
                  sx={{
                    ':first-of-type': { pl: 0 },
                    ':last-child': { pr: 0 },
                    '& .MuiStepConnector-root': { top: { xs: 6, sm: 12 } },
                  }}
                  key={label}
                >
                  <StepLabel
                    sx={{
                      '.MuiStepLabel-labelContainer': { maxWidth: '70px' },
                    }}
                  >
                    {label}
                  </StepLabel>
                </Step>
              ))}
            </Stepper>
            {activeStep === steps.length ? (
              <Stack spacing={2} useFlexGap>
                <Typography variant='h1'>📦</Typography>
                <Typography variant='h5'>Thank you for your order!</Typography>
                <Typography variant='body1' color='text.secondary'>
                  Your order number is
                  <strong>&nbsp;{orderResponseId}</strong>. We have emailed your
                  order confirmation and will update you once its shipped.
                </Typography>
                <Button
                  variant='contained'
                  sx={{
                    alignSelf: 'start',
                    width: { xs: '100%', sm: 'auto' },
                  }}
                  onClick={() => navigate(`/orders/${orderResponseId}`)}
                >
                  Go to my orders
                </Button>
              </Stack>
            ) : (
              <>
                {getStepContent(activeStep)}
                <Box
                  sx={{
                    display: 'flex',
                    flexDirection: { xs: 'column-reverse', sm: 'row' },
                    justifyContent:
                      activeStep !== 0 ? 'space-between' : 'flex-end',
                    alignItems: 'end',
                    flexGrow: 1,
                    gap: 1,
                    pb: { xs: 12, sm: 0 },
                    mt: { xs: 2, sm: 0 },
                    mb: '60px',
                  }}
                >
                  {activeStep !== 0 && (
                    <Button
                      startIcon={<ChevronLeftRoundedIcon />}
                      variant='text'
                      aria-label={getPrevBtnAriaLabel(activeStep)}
                      sx={{
                        display: { xs: 'none', sm: 'flex' },
                      }}
                      onClick={handleBack}
                    >
                      Previous
                    </Button>
                  )}

                  {activeStep !== 0 && (
                    <Button
                      startIcon={<ChevronLeftRoundedIcon />}
                      variant='outlined'
                      fullWidth
                      aria-label={getPrevBtnAriaLabel(activeStep)}
                      sx={{
                        display: { xs: 'flex', sm: 'none' },
                      }}
                      onClick={handleBack}
                    >
                      Previous
                    </Button>
                  )}

                  <Button
                    variant='contained'
                    endIcon={<ChevronRightRoundedIcon />}
                    aria-label={getNextBtnAriaLabel(activeStep)}
                    sx={{
                      width: { xs: '100%', sm: 'fit-content' },
                    }}
                    onClick={handleNext}
                  >
                    {activeStep === steps.length - 1 ? 'Place order' : 'Next'}
                  </Button>
                </Box>
              </>
            )}
          </Box>
        </Grid>
      </Grid>
    </>
  );
}
