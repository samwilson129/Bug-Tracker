import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/disableOnClickFunctions.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/notification/src/vaadin-notification.js';
import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';
import 'Frontend/generated/jar-resources/ReactRouterOutletElement.tsx';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '82a296d851797984ac8c157842f3a9265b0a69492cbb2d7b8a929c64437a0240') {
    pending.push(import('./chunks/chunk-2a2a5c9a960489a2b7b4e7d779e77b22b704e94df3d7a24255f7b28df68e6c1f.js'));
  }
  if (key === '4def31c488f68ee50d622a5581197a4b94d9e305b65f284cebc782df8002e30c') {
    pending.push(import('./chunks/chunk-e1ba28b1310465a3a8792e71167f7e86094468b69ab6bdd5b119cc0e88c1bfd2.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}