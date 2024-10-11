import * as React from 'react';
import { alpha } from '@mui/material';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Container from '@mui/material/Container';
import Link from '@mui/material/Link';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';

import heroImgUrl from '../../assets/hero-01.jpg';

import { brand, gray } from './getLandingPageTheme.js';

export default function Hero() {
  return (
    <Box
      id='hero'
      sx={theme => ({
        width: '100%',
        // TODO find a way to not hard-code the backgroundImage
        backgroundImage:
          theme.palette.mode === 'light'
            ? `linear-gradient(180deg, ${brand[100]}, #FFF)`
            : `linear-gradient(${brand[800]}, ${alpha('#090E10', 0.0)})`,
        backgroundSize: '100% 20%',
        backgroundRepeat: 'no-repeat',
      })}
    >
      <Container
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          pt: { xs: 14, sm: 20 },
          pb: { xs: 8, sm: 12 },
        }}
      >
        <Stack spacing={2} useFlexGap sx={{ width: { xs: '100%', sm: '70%' } }}>
          <Typography
            variant='h1'
            sx={{
              display: 'flex',
              flexDirection: { xs: 'column', md: 'row' },
              alignSelf: 'center',
              textAlign: 'center',
              fontSize: 'clamp(3.5rem, 10vw, 4rem)',
            }}
          >
            Our latest&nbsp;
            <Typography
              component='span'
              variant='h1'
              sx={{
                fontSize: 'clamp(3rem, 10vw, 4rem)',
                color: theme =>
                  theme.palette.mode === 'light'
                    ? 'primary.main'
                    : 'primary.light',
              }}
            >
              products
            </Typography>
          </Typography>
          <Typography
            textAlign='center'
            color='text.secondary'
            sx={{ alignSelf: 'center', width: { sm: '100%', md: '80%' } }}
          >
            Explore our cutting-edge products, delivering high-quality solutions
            tailored to your needs. Elevate your experience with top-tier
            features and services.
          </Typography>
          <Stack
            direction={{ xs: 'column', sm: 'row' }}
            alignSelf='center'
            spacing={1}
            useFlexGap
            sx={{ pt: 2, width: { xs: '100%', sm: 'auto' } }}
          >
            <TextField
              id='outlined-basic'
              hiddenLabel
              size='small'
              variant='outlined'
              aria-label='Enter your email address'
              placeholder='Your email address'
              inputProps={{
                autoComplete: 'off',
                'aria-label': 'Enter your email address',
              }}
            />
            <Button variant='contained' color='primary'>
              Start now
            </Button>
          </Stack>
          <Typography
            variant='caption'
            textAlign='center'
            sx={{ opacity: 0.8 }}
          >
            By clicking &quot;Start now&quot; you agree to our&nbsp;
            <Link href='#' color='primary'>
              Terms & Conditions
            </Link>
            .
          </Typography>
        </Stack>
        <Box
          id='image'
          sx={theme => ({
            mt: { xs: 8, sm: 10 },
            alignSelf: 'center',
            height: { xs: 200, sm: 700 },
            width: '100%',
            backgroundImage: `url(${heroImgUrl})`,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            borderRadius: '10px',
            outline: '1px solid',
            // TODO find a way to not hard-code the outlineColor
            outlineColor:
              theme.palette.mode === 'light'
                ? alpha(gray[300], 0.5)
                : alpha(brand[200], 0.1),
            // TODO find a way to not hard-code the boxShadow
            boxShadow:
              theme.palette.mode === 'light'
                ? `0 0 12px 8px ${alpha(brand[200], 0.2)}`
                : `0 0 24px 12px ${alpha(brand[700], 0.2)}`,
          })}
        />
      </Container>
    </Box>
  );
}
