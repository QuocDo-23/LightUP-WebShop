/**
 * Dùng cho: profile, address, payment, contact khi người dùng nhập vào số điện thoại
 */

(function () {

    const VALID_PREFIXES = [
        '03','05','07','08','09'   // đầu số
    ];

    function getPhoneError(value) {
        const trimmed = value.trim();

        if (trimmed === '') {
            return 'Vui lòng nhập số điện thoại';
        }

        if (/[^0-9]/.test(trimmed)) {
            return 'Số điện thoại chỉ được chứa chữ số, không dùng dấu cách hay ký tự đặc biệt';
        }

        if (!trimmed.startsWith('0')) {
            return 'Số điện thoại phải bắt đầu bằng số 0 (ví dụ: 0912345678)';
        }

        if (trimmed.length < 10) {
            return `Số điện thoại còn thiếu ${10 - trimmed.length} chữ số (cần đúng 10 số)`;
        }

        if (trimmed.length > 10) {
            return `Số điện thoại bị thừa ${trimmed.length - 10} chữ số (cần đúng 10 số)`;
        }

        const prefix = trimmed.substring(0, 2);
        if (!VALID_PREFIXES.includes(prefix)) {
            return `Đầu số "${prefix}" không hợp lệ. Số điện thoại Việt Nam bắt đầu bằng: 03x, 05x, 07x, 08x, 09x.`;
        }

        return null;
    }

    function getOrCreateErrorEl(input) {
        const id = '__phone_err_' + (input.id || input.name || Math.random().toString(36).slice(2)) + '__';
        let el = input.parentElement.querySelector('[data-phone-err]');
        if (!el) {
            el = document.createElement('p');
            el.setAttribute('data-phone-err', '1');
            el.style.cssText = [
                'color:#dc3545',
                'font-size:12.5px',
                'margin-top:4px',
                'margin-bottom:0',
                'display:none',
                'line-height:1.4',
            ].join(';');
            input.insertAdjacentElement('afterend', el);
        }
        return el;
    }

    function showError(input, msgEl) {
        input.style.borderColor = '#dc3545';
        input.style.boxShadow = '0 0 0 2px rgba(220,53,69,.18)';
        msgEl.style.color = '#dc3545';
        msgEl.textContent = undefined;
        msgEl.style.display = 'block';
    }

    function showSuccess(input, msgEl) {
        input.style.borderColor = '#28a745';
        input.style.boxShadow = '0 0 0 2px rgba(40,167,69,.18)';
        msgEl.style.color = '#28a745';
        msgEl.textContent = undefined;
        msgEl.style.display = 'block';
    }

    function clearMsg(input, msgEl) {
        input.style.borderColor = '';
        input.style.boxShadow = '';
        msgEl.style.display = 'none';
        msgEl.textContent = '';
    }

    function attachValidation(input) {
        const msgEl = getOrCreateErrorEl(input);
        let touched = false;

        input.addEventListener('blur', function () {
            touched = true;
            if (this.value.trim() === '') { clearMsg(this, msgEl); return; }
            const err = getPhoneError(this.value);
            if (err) showError(this, msgEl, err);
            else showSuccess(this, msgEl);
        });

        input.addEventListener('input', function () {
            if (!touched) return;
            if (this.value.trim() === '') { clearMsg(this, msgEl); return; }
            const err = getPhoneError(this.value);
            if (err) showError(this, msgEl, err);
            else showSuccess(this, msgEl);
        });

        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                touched = true;
                const err = getPhoneError(input.value);
                if (err) {
                    e.preventDefault();
                    e.stopImmediatePropagation();
                    showError(input, msgEl, err);
                    input.focus();
                } else {
                    showSuccess(input, msgEl);
                }
            }, { capture: true });
        }
    }

    function init() {
        document.querySelectorAll(
            'input[type="tel"], input[name="phone"], input[name="so-dien-thoai"]'
        ).forEach(function (input) {
            if (input.readOnly) return;
            attachValidation(input);
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();