import { Box, Button, Typography } from "@mui/material"
import { useAppSelector } from "../../app/hooks"
import { selectSubProviders } from "./subProviderSlice"
import { Link } from "react-router-dom"
import AddIcon from "@mui/icons-material/Add"

export const SubProviderList = () => {
  const subproviders = useAppSelector(selectSubProviders)

  return (
    <Box maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box
        display="flex"
        justifyContent="flex-end"
        style={{ marginBottom: "1rem" }}
      >
        <Button
          variant="contained"
          color="secondary"
          component={Link}
          to="/subproviders/create"
          startIcon={<AddIcon />}
        >
          NOVO
        </Button>
      </Box>

      {subproviders.map(subprovider => (
        <Typography>{subprovider.name}</Typography>
      ))}
    </Box>
  )
}
