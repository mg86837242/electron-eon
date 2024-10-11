// @index(['./*.{js,jsx}', './*/index.{js,jsx}'], f => `export { default as ${f.name === 'index' ? f.path.replace(/.*\/(\w+)\/index$/, '$1') : f.name} } from '${f.path.replace(/\/index$/, '')}';`)
export { default as CartPage } from './CartPage';
export { default as CheckoutPage } from './CheckoutPage';
export { default as LandingPage } from './LandingPage';
export { default as OrderById } from './OrderById';
export { default as Orders } from './Orders';
export { default as ProductsPage } from './ProductsPage';
export { default as Protected } from './Protected';
export { default as SignInSide } from './SignInSide';
export { default as SignUp } from './SignUp';
// @endindex
