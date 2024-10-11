import { styled } from '@mui/material/styles';
import TableCell from '@mui/material/TableCell';

export const TableCellHead = styled(TableCell)(({ theme }) => ({
  color: theme.palette.grey[900],
  fontWeight: 'bold',
}));

export const TableCellBody = styled(TableCell)(({ theme }) => ({
  backgroundColor: theme.palette.background.paper,
}));

export const TableCellBodyRight = styled(TableCell)(({ theme }) => ({
  backgroundColor: theme.palette.background.paper,
  textAlign: 'right',
  paddingRight: theme.spacing(1.25),
}));
