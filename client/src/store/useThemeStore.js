import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useThemeStore = create(
  persist(
    (set, get) => {
      const preferredColorMode = window.matchMedia(
        '(prefers-color-scheme: dark)',
      ).matches
        ? 'dark'
        : 'light';

      return {
        // The initial value `preferredColorMode` will be short-circuited if the `colorMode` state has been updated by any action (e.g., the toggle handler), as the persist middleware first evaluates the state persisted in the `localStorage`
        colorMode: preferredColorMode,
        toggleColorMode: () =>
          set({ colorMode: get().colorMode === 'light' ? 'dark' : 'light' }),
      };
    },
    {
      name: `${import.meta.env.VITE_APP_NAME}-color-mode`,
    },
  ),
);

export default useThemeStore;

// References:
// -- providing and caching for color mode ctx value, color mode's action (wrapped in an obj then cached) and theme ctx
//    value: https://mui.com/material-ui/customization/dark-mode/#toggling-color-mode
// -- system preference: https://mui.com/material-ui/customization/dark-mode/#system-preference
