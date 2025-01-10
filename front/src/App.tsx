import { Box, ThemeProvider, Typography } from "@mui/material"
import { Header } from "./components/Header"
import { Layout } from "./components/Layout"
import { appTheme } from "./config/theme"
import { Routes, Route } from "react-router-dom"
import { SubProviderList } from "./features/subprovider/ListSubProvider"
import { SubProviderCreate } from "./features/subprovider/CreateSubProvider"
import { SubProviderEdit } from "./features/subprovider/EditSubProvider"

const App = () => {
  return (
    <ThemeProvider theme={appTheme}>
      <Box
        component="main"
        sx={{
          height: "100vh",
          backgroundColor: theme => theme.palette.grey[900],
        }}
      >
        <Header></Header>
        <Layout>
          <Routes>
            <Route path="/" element={<SubProviderList />} />
            <Route path="/subproviders" element={<SubProviderList />} />
            <Route
              path="/subproviders/create"
              element={<SubProviderCreate />}
            />
            <Route
              path="/subproviders/edit/:id"
              element={<SubProviderEdit />}
            />

            <Route
              path="*"
              element={
                <Box sx={{ color: "white" }}>
                  <Typography variant="h1">404</Typography>
                  <Typography variant="h2">Not Found</Typography>
                </Box>
              }
            />
          </Routes>
        </Layout>
      </Box>
    </ThemeProvider>
  )
}

export default App
