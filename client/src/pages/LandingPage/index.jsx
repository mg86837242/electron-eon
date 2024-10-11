import * as React from 'react';
import { Box } from '@mui/material';
import Divider from '@mui/material/Divider';

import { AppAppBar, Footer } from '../../components';

import Faq from './Faq';
import Features from './Features';
import Hero from './Hero';
import Highlights from './Highlights';
import LogoCollection from './LogoCollection';
import Pricing from './Pricing';
import Testimonials from './Testimonials';

export default function LandingPage() {
  return (
    <>
      <AppAppBar />
      <Hero />
      <Box sx={{ bgcolor: 'background.default' }}>
        <LogoCollection />
        <Features />
        <Divider />
        <Testimonials />
        <Divider />
        <Highlights />
        <Divider />
        <Pricing />
        <Divider />
        <Faq />
        <Divider />
        <Footer />
      </Box>
    </>
  );
}
