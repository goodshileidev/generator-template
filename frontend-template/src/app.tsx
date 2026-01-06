import {localizeCodeList} from '@/common/code_list/code_list_static';
import {AvatarDropdown, AvatarName, Footer, Question, SelectLang} from '@/components';
import {getCurrentUser as fetchCurrentUser} from '@/common/service/login';
import {clearCurrentUser, getCurrentUser as getCachedUser, setCurrentUser} from '@/common/util/user_storage';
import i18n from '@/lang/i18n';
import {concatUrls} from '@/utils';
import {getToken, removeToken} from '@/utils/jwt';
import {LinkOutlined} from '@ant-design/icons';
import type {Settings as LayoutSettings} from '@ant-design/pro-components';
import {SettingDrawer} from '@ant-design/pro-components';
import {loader} from '@monaco-editor/react';
import {history, Link} from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
// import SelectProject from './components/select_project/select-project-index';
// import SinglePageSelect from './components/single_page_select';
import {errorConfig} from './requestErrorConfig';

localizeCodeList(i18n.t);
const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';
loader.config({
  paths: {
    vs: concatUrls(window.location.origin, 'scripts/vs'),
  },
});

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      const response = await fetchCurrentUser();
      const user = response?.data as API.CurrentUser;
      if (user) {
        setCurrentUser(user);
        return user;
      }
    } catch (error) {
      removeToken();
      clearCurrentUser();
      history.push(loginPath);
    }
    return undefined;
  };

  const token = getToken();
  const cachedUser = getCachedUser() as API.CurrentUser | undefined;
  const {location} = history;

  if (token && ![loginPath, '/user/register', '/user/register-result'].includes(location.pathname)) {
    const currentUser = cachedUser || (await fetchUserInfo());
    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }

  return {
    fetchUserInfo,
    currentUser: cachedUser,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {
  return {
    actionsRender: () => [
      // <SinglePageSelect key="singlePage"/>,
      // <SelectProject key="project"/>,
      <Question key="doc"/>,
      <SelectLang key="SelectLang"/>,
    ],
    avatarProps: {
      src: initialState?.currentUser?.avatar,
      title: <AvatarName/>,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.name,
    },
    footerRender: () => <Footer/>,
    onPageChange: () => {
      const {location} = history;
      // 如果没有登录，重定向到 login
      if (!initialState?.currentUser && location.pathname !== loginPath) {
        history.push(loginPath);
      }
    },
    bgLayoutImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: isDev
      ? [
        <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
          <LinkOutlined/>
          <span>OpenAPI 文档</span>
        </Link>,
      ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {isDev && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request: RequestConfig = {
  baseURL: '/api',
  ...errorConfig,
};
