/**
 * Dùng cho: login, register, forgot_password, profile, address, payment, contact để validate khi người dùng nhập mail
 */
(function () {
    function getEmailError(value) {
        const trimmed = value.trim();

        if (trimmed === '') {
            return 'Vui lòng nhập email';
        }

        if (!trimmed.includes('@')) {
            return 'Email thiếu ký tự "@". Ví dụ: example@gmail.com';
        }

        const parts = trimmed.split('@');

        if (parts.length > 2) {
            return 'Email chỉ được chứa một ký tự "@"';
        }

        const localPart = parts[0];  // trước @
        const domainPart = parts[1]; // sau @

        if (!localPart || localPart.length === 0) {
            return 'Email thiếu tên người dùng trước "@". Ví dụ: example@gmail.com';
        }

        if (!domainPart || domainPart.length === 0) {
            return 'Email thiếu tên miền sau "@". Ví dụ: example@gmail.com';
        }

        if (!domainPart.includes('.')) {
            return 'Tên miền email thiếu dấu "." (ví dụ: gmail.com, yahoo.com)';
        }

        const domainSegments = domainPart.split('.');

        if (domainSegments[0] === '') {
            return 'Tên miền không được bắt đầu bằng dấu "."';
        }

        const tld = domainSegments[domainSegments.length - 1];
        if (!tld || tld.length < 2) {
            return 'Đuôi tên miền không hợp lệ (ví dụ: .com, .vn, .net)';
        }

        const localValid = /^[a-zA-Z0-9._%+\-]+$/.test(localPart);
        if (!localValid) {
            return 'Tên người dùng trong email chứa ký tự không hợp lệ';
        }

        const domainValid = /^[a-zA-Z0-9.\-]+$/.test(domainPart);
        if (!domainValid) {
            return 'Tên miền email chứa ký tự không hợp lệ';
        }

        if (/\.{2,}/.test(trimmed)) {
            return 'Email không được chứa hai dấu "." liền nhau';
        }

        if (localPart.startsWith('.') || localPart.endsWith('.')) {
            return 'Tên người dùng không được bắt đầu hoặc kết thúc bằng dấu "."';
        }

        return null;
    }

    function getOrCreateErrorEl(input) {
        const id = '__email_err__';
        let el = input.parentElement.querySelector('#' + id);
        if (!el) {
            el = document.createElement('p');
            el.id = id;
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
            const err = getEmailError(this.value);
            if (err) showError(this, errorEl, err);
            else clearError(this, errorEl);
        });

        input.addEventListener('input', function () {
            if (!touched) return;
            const err = getEmailError(this.value);
            if (err) showError(this, errorEl, err);
            else clearError(this, errorEl);
        });

        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                touched = true;
                const err = getEmailError(input.value);
                if (err) {
                    e.preventDefault();
                    showError(input, errorEl, err);
                    input.focus();
                } else {
                    clearError(input, errorEl);
                }
            }, { capture: true });
        }
    }

    function init() {
        const emailInputs = document.querySelectorAll(
            'input[type="email"], input[name="email"]'
        );
        emailInputs.forEach(function (input) {
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