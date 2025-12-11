import {ProLayoutProps} from '@ant-design/pro-components';

/**
 * @name
 */
const Settings: ProLayoutProps & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  colorPrimary: '#13C2C2',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: true,
  fixSiderbar: true,
  pwa: true,
  logo: 'https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg',
  splitMenus: true,
  siderMenuType: 'group',
  siderWidth: 200,
  colorWeak: false,
  title: 'AI辅助系统开发平台',
  iconfontUrl: '',
  token: {
    // pageContainer: {
    //   paddingBlockPageContainerContent: 0,
    //   paddingInlinePageContainerContent: 5,
    // },
    // 参见ts声明，demo 见文档，通过token 修改样式
    //https://procomponents.ant.design/components/layout#%E9%80%9A%E8%BF%87-token-%E4%BF%AE%E6%94%B9%E6%A0%B7%E5%BC%8F
  },
};

export default Settings;
