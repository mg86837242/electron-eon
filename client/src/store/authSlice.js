const createAuthSlice = (set, get) => ({
  // The initial value empty str will be short-circuited if the `token` state has been updated by any action (e.g., `updateToken`), as the persist middleware first evaluates the state persisted in the `localStorage`
  token: '',
  updateToken: newToken => set({ token: newToken }),
});

export default createAuthSlice;

// References:
// -- "If a state called `token` is stored in Zustand's store (e.g.,
//    `useAuthStore`) and the `token` is automatically persisted in the
//    `localStorage` by using Zustand's persist middleware, when the user
//    tampers with the token string in the `localStorage`, will Zustand's
//    `token` state within the `useAuthStore` reflect that?" => theory
//    (unreliable):
//    - Behavior:
//      1. Initial Load: When the application loads, Zustand reads the token
//         from `localStorage` and initializes the store with this value.
//      2. Tampering: If the user changes the token value in `localStorage`
//         manually, the change will not be reflected in the Zustand store
//         immediately. However, if the application reloads or reinitializes
//         the store (e.g., navigating away and back) or has the logic to
//         programmatically read the from `localStorage` at runtime, the
//         tampered value will be read from `localStorage` and used to set the
//         state in the store.
//    - Tampering with `localStorage`: If the user manually changes the token
//      string in `localStorage` (e.g., using browser developer tools), the next
//      time the application reads from `localStorage`, the Zustand store will
//      reflect this change. This typically happens:
//      1. On Application Reload: When the application reloads, Zustand will
//         initialize the store with the values from `localStorage`. If the
//         token in `localStorage` has been tampered with, the store will
//         reflect this tampered value.
//      2. On Manual Read: If the application has logic to manually read from
//         `localStorage` at runtime, the store will reflect the tampered value
//         immediately after such read operation.
