import { styled } from '@mui/material/styles';
import Typography from '@mui/material/Typography';

export const TwoLineEllipsisH5 = styled(Typography)(({ theme }) => {
  const lineHeight =
    parseFloat(theme.typography.h5.lineHeight) * theme.typography.h5.fontSize;

  return {
    display: '-webkit-box',
    overflow: 'hidden',
    WebkitBoxOrient: 'vertical',
    WebkitLineClamp: 2,
    textOverflow: 'ellipsis',
    lineHeight: theme.typography.h5.lineHeight,
    minHeight: `${2 * lineHeight}px`,
  };
});

export const TwoLineEllipsisBody2 = styled(Typography)(({ theme }) => {
  const lineHeight =
    parseFloat(theme.typography.body2.lineHeight) *
    theme.typography.body2.fontSize;

  return {
    display: '-webkit-box',
    overflow: 'hidden',
    WebkitBoxOrient: 'vertical',
    WebkitLineClamp: 2,
    textOverflow: 'ellipsis',
    lineHeight: theme.typography.body2.lineHeight,
    minHeight: `${2 * lineHeight}px`,
  };
});
