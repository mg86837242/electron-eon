const createCheckoutSlice = set => ({
  checkoutItems: [],
  hasVisitedCheckout: false,
  orderResponse: null,
  updateCheckoutItems: newCheckoutItems =>
    set({ checkoutItems: newCheckoutItems }),
  updateHasVisitedCheckout: newHasVisitedCheckout =>
    set({ hasVisitedCheckout: newHasVisitedCheckout }),
  updateOrderResponse: newOrderResponse =>
    set({ orderResponse: newOrderResponse }),
});

// TODO use `checkoutItems` state for optimistic UI && guest checkout
export default createCheckoutSlice;
