(function () {

    function getPasswordError(value) {
        if (value === '') return 'Vui lòng nhập mật khẩu';
        if (value.length < 6) return `Mật khẩu quá ngắn, cần ít nhất 6 ký tự (hiện có ${value.length} kí tự)`;
        if (!/[a-zA-Z]/.test(value)) return 'Mật khẩu phải chứa ít nhất một chữ cái';
        if (!/[0-9]/.test(value)) return 'Mật khẩu phải chứa ít nhất một chữ số';
        return null;
    }

    function getConfirmError(password, confirm) {
        if (confirm === '') return 'Vui lòng nhập lại mật khẩu';
        if (confirm !== password) return 'Mật khẩu xác nhận không khớp';
        return null;
    }

    function getOrCreateMsgEl(input, attr) {
        let el = input.parentElement.querySelector('[' + attr + ']');
        if (!el) {
            el = document.createElement('p');
            el.setAttribute(attr, '1');
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
        msgEl.textContent =  msg;
        msgEl.style.display = 'block';
    }

    function clearMsg(input, msgEl) {
        input.style.borderColor = '';
        input.style.boxShadow = '';
        msgEl.style.display = 'none';
        msgEl.textContent = '';
    }

    function init() {
        const pwInput = document.querySelector('input[name="password"]');
        const cfInput = document.querySelector('input[name="confirmPassword"]');

        if (!pwInput) return;

        const pwMsg = getOrCreateMsgEl(pwInput, 'data-pw-msg');

        let pwTouched = false;

        pwInput.addEventListener('blur', function () {
            pwTouched = true;
            if (this.value === '') { clearMsg(this, pwMsg); return; }
            const err = getPasswordError(this.value);
            if (err) showError(this, pwMsg, err);
            else showSuccess(this, pwMsg);
            if (cfInput && cfTouched) validateConfirm();
        });

        pwInput.addEventListener('input', function () {
            if (!pwTouched) return;
            if (this.value === '') { clearMsg(this, pwMsg); return; }
            const err = getPasswordError(this.value);
            if (err) showError(this, pwMsg, err);
            else showSuccess(this, pwMsg);
            if (cfInput && cfTouched) validateConfirm();
        });

        if (!cfInput) return;

        const cfMsg = getOrCreateMsgEl(cfInput, 'data-cf-msg');
        let cfTouched = false;

        function validateConfirm() {
            if (cfInput.value === '') { clearMsg(cfInput, cfMsg); return; }
            const err = getConfirmError(pwInput.value, cfInput.value);
            if (err) showError(cfInput, cfMsg, err);
            else showSuccess(cfInput, cfMsg);
        }

        cfInput.addEventListener('blur', function () {
            cfTouched = true;
            validateConfirm();
        });

        cfInput.addEventListener('input', function () {
            if (!cfTouched) return;
            validateConfirm();
        });

        const form = pwInput.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                pwTouched = true;
                cfTouched = true;

                const pwErr = getPasswordError(pwInput.value);
                if (pwErr) {
                    e.preventDefault();
                    e.stopImmediatePropagation();
                    showError(pwInput, pwMsg, pwErr);
                    pwInput.focus();
                    return;
                } else {
                    showSuccess(pwInput, pwMsg);
                }

                if (cfInput) {
                    const cfErr = getConfirmError(pwInput.value, cfInput.value);
                    if (cfErr) {
                        e.preventDefault();
                        e.stopImmediatePropagation();
                        showError(cfInput, cfMsg, cfErr);
                        cfInput.focus();
                    } else {
                        showSuccess(cfInput, cfMsg);
                    }
                }
            }, { capture: true });
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();