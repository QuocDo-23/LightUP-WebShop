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

    function getOrCreateMsgEl(input) {
        let el = input.parentElement.querySelector('[data-email-msg]');
        if (!el) {
            el = document.createElement('p');
            el.setAttribute('data-email-msg', '1');
            el.style.cssText = 'font-size:12.5px;margin-top:4px;margin-bottom:0;display:none;line-height:1.4;';
            input.insertAdjacentElement('afterend', el);
        }
        return el;
    }

    function showError(input, msgEl, msg) {
        input.style.borderColor = '#dc3545';
        input.style.boxShadow = '0 0 0 2px rgba(220,53,69,.18)';
        msgEl.style.color = '#dc3545';
        msgEl.textContent = msg;
        msgEl.style.display = 'block';
    }

    function showSuccess(input, msgEl, msg) {
        input.style.borderColor = '#28a745';
        input.style.boxShadow = '0 0 0 2px rgba(40,167,69,.18)';
        msgEl.style.color = '#28a745';
        msgEl.textContent = msg;
        msgEl.style.display = 'block';
    }

    function clearMsg(input, msgEl) {
        input.style.borderColor = '';
        input.style.boxShadow = '';
        msgEl.style.display = 'none';
        msgEl.textContent = '';
    }

    function attachValidation(input) {
        const msgEl = getOrCreateMsgEl(input);
        let touched = false;

        input.addEventListener('blur', function () {
            touched = true;
            const err = getEmailError(this.value);
            if (this.value.trim() === '') { clearMsg(this, msgEl); return; }
            if (err) showError(this, msgEl, err);
            else showSuccess(this, msgEl);
        });

        input.addEventListener('input', function () {
            if (!touched) return;
            const err = getEmailError(this.value);
            if (this.value.trim() === '') { clearMsg(this, msgEl); return; }
            if (err) showError(this, msgEl, err);
            else showSuccess(this, msgEl);
        });

        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                touched = true;
                const err = getEmailError(input.value);
                if (err) {
                    e.preventDefault();
                    showError(input, msgEl, err);
                    input.focus();
                } else {
                    showSuccess(input, msgEl);
                }
            }, { capture: true });
        }
    }

    function checkEmailExists(input, msgEl, email, onResult) {
        msgEl.style.color   = '#6c757d';
        msgEl.textContent   = ' ';
        msgEl.style.display = 'block';

        const ctx = document.querySelector('meta[name="contextPath"]')
            ? document.querySelector('meta[name="contextPath"]').content
            : '';

        fetch(ctx + '/check-email?email=' + encodeURIComponent(email), {
            method: 'GET',
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(function (res) { return res.json(); })
            .then(function (data) {
                if (data.exists) {
                    const loginUrl = (ctx || '') + '/login';
                    showError(input, msgEl,
                        'Email này đã có tài khoản. '
                        + '<a href="' + loginUrl + '" '
                        + 'style="color:#dc3545;font-weight:600;text-decoration:underline;">'
                        + 'Đăng nhập ngay</a>'
                    );
                    onResult(false);
                } else {
                    showSuccess(input, msgEl);
                    onResult(true);
                }
            })
            .catch(function () {
                showSuccess(input, msgEl);
                onResult(true);
            });
    }

    function attachValidation(input) {
        const msgEl     = getOrCreateMsgEl(input);
        const isRegister = !!document.getElementById('registerForm');
        let touched      = false;
        let emailValid   = true;

        function validate(onDone) {
            const val = input.value;
            if (val.trim() === '') { clearMsg(input, msgEl); emailValid = true; if (onDone) onDone(); return; }

            const err = getEmailError(val);
            if (err) {
                showError(input, msgEl, err);
                emailValid = false;
                if (onDone) onDone();
                return;
            }

            if (isRegister) {
                checkEmailExists(input, msgEl, val.trim(), function (ok) {
                    emailValid = ok;
                    if (onDone) onDone();
                });
            } else {
                showSuccess(input, msgEl);
                emailValid = true;
                if (onDone) onDone();
            }
        }

        input.addEventListener('blur', function () {
            touched = true;
            validate();
        });

        input.addEventListener('input', function () {
            if (!touched) return;
            validate();
        });

        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                if (!touched || !emailValid) {
                    e.preventDefault();
                    touched = true;
                    validate(function () {
                        if (!emailValid) input.focus();
                    });
                }
            }, { capture: true });
        }
    }

    function init() {
        document.querySelectorAll('input[type="email"], input[name="email"]').forEach(function (input) {
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