import {Footer} from '@/components';
import {fetchCaptcha, login} from '@/common/service/login';
import {setCurrentUser} from '@/common/util/user_storage';
import {sha256Hex} from '@/utils/crypto';
import {
  AlipayCircleOutlined,
  LockOutlined,
  ReloadOutlined,
  SafetyCertificateOutlined,
  TaobaoCircleOutlined,
  UserOutlined,
  WeiboCircleOutlined,
} from '@ant-design/icons';
import {LoginForm, ProFormCheckbox, ProFormInstance, ProFormText} from '@ant-design/pro-components';
import {FormattedMessage, Helmet, SelectLang, useIntl, useModel} from '@umijs/max';
import {Alert, message} from 'antd';
import {createStyles} from 'antd-style';
import React, {useCallback, useEffect, useRef, useState} from 'react';
import {flushSync} from 'react-dom';
import Settings from '../../../../config/defaultSettings';

const useStyles = createStyles(({token}) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

const ActionIcons = () => {
  const {styles} = useStyles();

  return (
    <>
      <AlipayCircleOutlined key="AlipayCircleOutlined" className={styles.action}/>
      <TaobaoCircleOutlined key="TaobaoCircleOutlined" className={styles.action}/>
      <WeiboCircleOutlined key="WeiboCircleOutlined" className={styles.action}/>
    </>
  );
};

const Lang = () => {
  const {styles} = useStyles();

  return (
    <div className={styles.lang} data-lang>
      {SelectLang && <SelectLang/>}
    </div>
  );
};

const LoginMessage: React.FC<{
  content: string;
}> = ({content}) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};

type AccountLoginValues = API.LoginParams & { captchaId?: string; captchaCode?: string };

const Login: React.FC = () => {
  const [errorMessage, setErrorMessage] = useState<string>();
  const [captchaImage, setCaptchaImage] = useState<string>('');
  const [captchaLoading, setCaptchaLoading] = useState(false);
  const formRef = useRef<ProFormInstance>();
  const {initialState, setInitialState} = useModel('@@initialState');
  const {styles} = useStyles();
  const intl = useIntl();

  const fetchUserInfo = async (userInfoOverride?: API.CurrentUser) => {
    const userInfo = userInfoOverride || (await initialState?.fetchUserInfo?.());
    if (userInfo) {
      flushSync(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  const loadCaptcha = useCallback(async () => {
    try {
      setCaptchaLoading(true);
      const response = await fetchCaptcha();
      const payload = response?.data;
      if (payload) {
        setCaptchaImage(`data:image/png;base64,${payload.image}`);
        formRef.current?.setFieldsValue({captchaId: payload.captchaId, captchaCode: undefined});
      }
    } catch (error) {
      message.error(intl.formatMessage({id: 'pages.login.captcha.failure', defaultMessage: '获取验证码失败'}));
    } finally {
      setCaptchaLoading(false);
    }
  }, [intl]);

  useEffect(() => {
    loadCaptcha();
  }, [loadCaptcha]);

  const handleSubmit = async (values: AccountLoginValues) => {
    try {
      setErrorMessage(undefined);
      const encryptedPassword = await sha256Hex(values.password || '');
      const response = await login({
        username: values.username?.trim(),
        password: encryptedPassword,
        captchaId: values.captchaId,
        captchaCode: values.captchaCode,
      });
      if (response?.code === 200 && response?.data) {
        setCurrentUser(response.data);
        const defaultLoginSuccessMessage = intl.formatMessage({
          id: 'pages.login.success',
          defaultMessage: '登录成功！',
        });
        message.success(defaultLoginSuccessMessage);
        await fetchUserInfo(response.data as API.CurrentUser);
        const urlParams = new URL(window.location.href).searchParams;
        window.location.href = urlParams.get('redirect') || '/';
        return;
      }
      setErrorMessage(response?.message || intl.formatMessage({
        id: 'pages.login.failure',
        defaultMessage: '登录失败，请重试！',
      }));
    } catch (error: any) {
      const fallbackMessage = error?.message || intl.formatMessage({
        id: 'pages.login.failure',
        defaultMessage: '登录失败，请重试！',
      });
      setErrorMessage(fallbackMessage);
    } finally {
      loadCaptcha();
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {intl.formatMessage({
            id: 'menu.login',
            defaultMessage: '登录页',
          })}
          - {Settings.title}
        </title>
      </Helmet>
      <Lang/>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          formRef={formRef}
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg"/>}
          title="Ant Design"
          subTitle={intl.formatMessage({id: 'pages.layouts.userLayout.title'})}
          initialValues={{
            autoLogin: true,
          }}
          actions={[
            <FormattedMessage
              key="loginWith"
              id="pages.login.loginWith"
              defaultMessage="其他登录方式"
            />,
            <ActionIcons key="icons"/>,
          ]}
          onFinish={async (values) => {
            await handleSubmit(values as AccountLoginValues);
          }}
        >
          {errorMessage && <LoginMessage content={errorMessage}/>}
          <ProFormText
            name="username"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined/>,
            }}
            placeholder={intl.formatMessage({
              id: 'pages.login.username.placeholder',
              defaultMessage: '用户名',
            })}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.login.username.required"
                    defaultMessage="请输入用户名!"
                  />
                ),
              },
            ]}
          />
          <ProFormText.Password
            name="password"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined/>,
            }}
            placeholder={intl.formatMessage({
              id: 'pages.login.password.placeholder',
              defaultMessage: '密码',
            })}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.login.password.required"
                    defaultMessage="请输入密码！"
                  />
                ),
              },
            ]}
          />
          <ProFormText name="captchaId" hidden/>
          <ProFormText
            name="captchaCode"
            fieldProps={{
              size: 'large',
              prefix: <SafetyCertificateOutlined/>,
              addonAfter: (
                <div
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 8,
                    width: 110,
                  }}
                >
                  {captchaImage ? (
                    <img
                      src={captchaImage}
                      alt={intl.formatMessage({
                        id: 'pages.login.captcha.placeholder',
                        defaultMessage: '验证码',
                      })}
                      style={{
                        height: 32,
                        width: 80,
                        objectFit: 'cover',
                        cursor: 'pointer',
                      }}
                      onClick={loadCaptcha}
                    />
                  ) : (
                    <ReloadOutlined spin={captchaLoading}/>
                  )}
                  <ReloadOutlined
                    spin={captchaLoading}
                    style={{cursor: 'pointer'}}
                    onClick={loadCaptcha}
                  />
                </div>
              ),
            }}
            placeholder={intl.formatMessage({
              id: 'pages.login.captcha.placeholder',
              defaultMessage: '请输入验证码',
            })}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.login.captcha.required"
                    defaultMessage="请输入验证码！"
                  />
                ),
              },
            ]}
          />
          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              <FormattedMessage id="pages.login.rememberMe" defaultMessage="自动登录"/>
            </ProFormCheckbox>
            <a
              style={{
                float: 'right',
              }}
              href="/user/reset-password"
            >
              <FormattedMessage id="pages.login.forgotPassword" defaultMessage="忘记密码"/>
            </a>
          </div>
        </LoginForm>
      </div>
      <Footer/>
    </div>
  );
};

export default Login;
