const KEY = {
  fullscreenEnabled: 0,
  fullscreenElement: 1,
  requestFullscreen: 2,
  exitFullscreen: 3,
  fullscreenchange: 4,
  fullscreenerror: 5,
  fullscreen: 6,
} as const;

const WEBKIT = [
  'webkitFullscreenEnabled',
  'webkitFullscreenElement',
  'webkitRequestFullscreen',
  'webkitExitFullscreen',
  'webkitfullscreenchange',
  'webkitfullscreenerror',
  '-webkit-full-screen',
] as const;

const MOZ = [
  'mozFullScreenEnabled',
  'mozFullScreenElement',
  'mozRequestFullScreen',
  'mozCancelFullScreen',
  'mozfullscreenchange',
  'mozfullscreenerror',
  '-moz-full-screen',
] as const;

const MS = [
  'msFullscreenEnabled',
  'msFullscreenElement',
  'msRequestFullscreen',
  'msExitFullscreen',
  'MSFullscreenChange',
  'MSFullscreenError',
  '-ms-fullscreen',
] as const;

// so it doesn't throw if no window or document
const document =
  typeof window !== 'undefined' && typeof window.document !== 'undefined'
    ? window.document
    : {};

const vendor =
  ('fullscreenEnabled' in document && Object.keys(KEY)) ||
  (WEBKIT[0] in document && WEBKIT) ||
  (MOZ[0] in document && MOZ) ||
  (MS[0] in document && MS) ||
  [];

  // <K extends keyof WindowEventMap>(type: K, listener: (this: Window, ev: WindowEventMap[K]) => any, options?: boolean | AddEventListenerOptions): void

// prettier-ignore
export default {
  requestFullscreen: element => element[vendor[KEY.requestFullscreen]](),
  requestFullscreenFunction: element => element[vendor[KEY.requestFullscreen]],
  get exitFullscreen() { return document[vendor[KEY.exitFullscreen]].bind(document); },
  get fullscreenPseudoClass() { return `:${vendor[KEY.fullscreen]}`; },
  addEventListener: (type: string, handler, options?: boolean | AddEventListenerOptions) => document.addEventListener(vendor[KEY[type]], handler, options),
  removeEventListener: (type: string, handler: any, options?) => document.removeEventListener(vendor[KEY[type]], handler, options),
  get fullscreenEnabled() { return Boolean(document[vendor[KEY.fullscreenEnabled]]); },
  set fullscreenEnabled(val) {},
  get fullscreenElement() { return document[vendor[KEY.fullscreenElement]]; },
  set fullscreenElement(val) {},
  get onfullscreenchange() { return document[`on${vendor[KEY.fullscreenchange]}`.toLowerCase()]; },
  set onfullscreenchange(handler) { return document[`on${vendor[KEY.fullscreenchange]}`.toLowerCase()] = handler; },
  get onfullscreenerror() { return document[`on${vendor[KEY.fullscreenerror]}`.toLowerCase()]; },
  set onfullscreenerror(handler) { return document[`on${vendor[KEY.fullscreenerror]}`.toLowerCase()] = handler; },
};
