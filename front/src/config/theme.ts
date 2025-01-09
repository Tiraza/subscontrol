import { createTheme } from "@mui/material";

export const appTheme = createTheme({
    palette: {
        mode: 'dark',
        primary: {
            main: "#f5f5f1",
        },
        secondary: {
            main: "#E50914",
        },
        text: {
            primary: "#f5f5f1",
        },
    },
    typography: {
        fontFamily: "Arial, sans-serif",
    },
    shape: {
        borderRadius: 8,
    },
    breakpoints: {
        values: {
            xs: 0,
            sm: 600,
            md: 960,
            lg: 1280,
            xl: 1920,
        },
    },
});