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

    function showError(input, errorEl, msg) {
        input.style.borderColor = '#dc3545';
        input.style.boxShadow = '0 0 0 2px rgba(220,53,69,.18)';
        errorEl.textContent = msg;
        errorEl.style.display = 'block';
    }

    function clearError(input, errorEl) {
        input.style.borderColor = '';
        input.style.boxShadow = '';
        errorEl.style.display = 'none';
        errorEl.textContent = '';
    }

    function attachValidation(input) {
        const errorEl = getOrCreateErrorEl(input);
        let touched = false;

        input.addEventListener('blur', function () {
            touched = true;
            const err = getPhoneError(this.value);
            if (err) showError(this, errorEl, err);
            else clearError(this, errorEl);
        });

        input.addEventListener('input', function () {
            if (!touched) return;
            const err = getPhoneError(this.value);
            if (err) showError(this, errorEl, err);
            else clearError(this, errorEl);
        });

        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                touched = true;
                const err = getPhoneError(input.value);
                if (err) {
                    e.preventDefault();
                    e.stopImmediatePropagation();
                    showError(input, errorEl, err);
                    input.focus();
                } else {
                    clearError(input, errorEl);
                }
            }, { capture: true });
        }
    }

    function init() {
        const phoneInputs = document.querySelectorAll(
            'input[type="tel"], input[name="phone"], input[name="so-dien-thoai"]'
        );
        phoneInputs.forEach(function (input) {
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