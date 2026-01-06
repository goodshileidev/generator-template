import {createStyles} from 'antd-style';

const useStyles = createStyles(({token}) => {
  return {
    baseView: {
      display: 'flex',
      paddingTop: '12px',
      '.ant-legacy-form-item .ant-legacy-form-item-control-wrapper': {
        width: '100%',
      },
      [`@media screen and (max-width: ${token.screenXL}px)`]: {
        flexDirection: 'column-reverse',
      },
    },
    infoStack: {
      display: 'flex',
      flexDirection: 'column',
      gap: '16px',
      width: '100%',
    },
    helperAlert: {
      borderRadius: token.borderRadiusLG,
    },
    infoCard: {
      width: '100%',
      padding: '20px',
      background: token.colorBgContainer,
      borderRadius: token.borderRadiusLG,
      border: `${token.lineWidth}px solid ${token.colorSplit}`,
      boxShadow: token.boxShadowTertiary,
    },
    cardHeader: {
      display: 'flex',
      justifyContent: 'space-between',
      gap: '16px',
      flexWrap: 'wrap',
    },
    cardTitle: {
      fontSize: token.fontSizeLG,
      fontWeight: 600,
      color: token.colorText,
    },
    cardDescription: {
      marginBottom: 0,
      color: token.colorTextSecondary,
    },
    cardStatusRow: {
      marginTop: '16px',
      display: 'flex',
      alignItems: 'center',
      gap: '12px',
    },
    cardValue: {
      fontSize: token.fontSizeLG,
      color: token.colorText,
    },
    warningIcon: {
      fontSize: '28px',
      color: token.colorWarning,
    },
    mailIcon: {
      fontSize: '28px',
    },
    verifiedIcon: {
      color: token.colorSuccess,
    },
    pendingIcon: {
      color: token.colorWarning,
    },
    statusTag: {
      marginInlineEnd: 0,
    },
    left: {
      minWidth: '224px',
      maxWidth: '448px',
    },
    right: {
      flex: '1',
      paddingLeft: '104px',
      [`@media screen and (max-width: ${token.screenXL}px)`]: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        maxWidth: '448px',
        padding: '20px',
      },
    },
    avatar_title: {
      height: '22px',
      marginBottom: '8px',
      color: token.colorTextHeading,
      fontSize: token.fontSize,
      lineHeight: '22px',
      [`@media screen and (max-width: ${token.screenXL}px)`]: {
        display: 'none',
      },
    },
    avatar: {
      width: '144px',
      height: '144px',
      marginBottom: '12px',
      overflow: 'hidden',
      img: {width: '100%'},
    },
    button_view: {
      width: '144px',
      textAlign: 'center',
    },
    area_code: {
      width: '72px',
    },
    phone_number: {
      width: '214px',
    },
  };
});

export default useStyles;
