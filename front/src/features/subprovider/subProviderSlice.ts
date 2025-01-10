import { createAppSlice } from "../../app/createAppSlice"

interface SubProvider {
  id: String
  name: String
  is_active: boolean
  last_sync: null | string
}

const subProvider: SubProvider = {
  id: "3de3088f-82e2-4c19-b483-d226e86059bc",
  name: "Patreon",
  is_active: true,
  last_sync: null,
}

const initialState = [
  subProvider,
  {
    ...subProvider,
    id: "f86990fc-a9bf-4e63-974e-f406d91b4b97",
    name: "Kickstarter",
  },
  {
    ...subProvider,
    id: "eed9ff72-4af4-452e-9d0a-76994683a6b0",
    name: "Ko-fiKo-fi",
  },
]

export const subProviderSlice = createAppSlice({
  name: "subProviders",
  initialState,
  reducers: {
    createSubProvider(state, action) {},
    updateSubProvider(state, action) {},
    deleteSubProvider(state, action) {},
  },
  selectors: {
    selectSubProviders: state => state,
  },
})

export const { selectSubProviders } = subProviderSlice.selectors
