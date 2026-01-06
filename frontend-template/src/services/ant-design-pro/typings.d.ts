declare namespace API {
  type CurrentUser = {
    name?: string;
    avatar?: string;
    userid?: string;
    email?: string;
    signature?: string;
    title?: string;
    group?: string;
    tags?: { key?: string; label?: string }[];
    notifyCount?: number;
    unreadCount?: number;
    country?: string;
    access?: string;
    geographic?: {
      province?: { label?: string; key?: string };
      city?: { label?: string; key?: string };
    };
    address?: string;
    phone?: string;
  };

  type LoginResult = {
    code?: number;
    message?: string;
    success?: boolean;
    data?: CurrentUser;
  };

  type LoginParams = {
    username?: string;
    password?: string;
    captchaId?: string;
    captchaCode?: string;
    autoLogin?: boolean;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };
}
