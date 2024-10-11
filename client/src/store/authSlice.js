const createAuthSlice = (set, get) => ({
  // The initial value empty str will be short-circuited if the `token` state has been updated by any action (e.g., `updateToken`), as the persist middleware first evaluates the state persisted in the `localStorage`
  token: '',
  updateToken: newToken => set({ token: newToken }),
});

export default createAuthSlice;

// If a state called `token` is stored in Zustand's store (e.g.,
// `useAuthStore`) and the `token` is automatically persisted in the
// `localStorage` by using Zustand's persist middleware, when the user
// tampers with the token string in the `localStorage`, will Zustand's
// `token` state within the `useAuthStore` reflect that? => no
// TODO there's currently no code implementation to immediately log out
//  the user when the user tampers with, esp. deletes, token persisted
//  in `localStorage`
