/**
 * Dùng cho: register, profile, address, payment, contact
 *
 * Quy tắc hợp lệ:
 * Từ 3 đến 25 ký tự
 * Mỗi từ phải bắt đầu bằng chữ hoa (bao gồm tiếng Việt có dấu)
 * Nếu có 2 từ trở lên, giữa các từ phải có dấu cách
 * Không chứa ký tự đặc biệt (chỉ chữ cái, dấu cách)
 * Không có dấu cách thừa đầu/cuối hoặc nhiều dấu cách liên tiếp
 */

(function () {

    const NAME_SELECTORS = [
        'input[name="name"]',
        'input[name="fullName"]',
        'input[name="recipientName"]',
        'input[name="ho-ten"]',
    ].join(', ');

    function isUpperCase(ch) {
        return ch === ch.toUpperCase() && ch !== ch.toLowerCase();
    }

    function isLettersAndSpacesOnly(str) {
        return /^[\p{L} ]+$/u.test(str);
    }

    function getNameError(value) {
        const trimmed = value.trim();

        if (trimmed === '') {
            return 'Vui lòng nhập họ và tên';
        }

        if (!isLettersAndSpacesOnly(trimmed)) {
            return 'Họ và tên không được chứa số hoặc ký tự đặc biệt';
        }

        if (/  +/.test(trimmed)) {
            return 'Họ và tên không được có nhiều dấu cách liên tiếp';
        }

        if (trimmed.length < 3) {
            return `Họ và tên quá ngắn, cần ít nhất 3 ký tự (hiện có ${trimmed.length} kí tự)`;
        }

        if (trimmed.length > 25) {
            return `Họ và tên quá dài, tối đa 25 ký tự (hiện có ${trimmed.length} kí tự)`;
        }

        const words = trimmed.split(' ');
        for (let i = 0; i < words.length; i++) {
            const word = words[i];
            if (word.length === 0) continue;
            if (!isUpperCase(word[0])) {
                if (words.length === 1) {
                    return 'Tên phải bắt đầu bằng chữ hoa (ví dụ: Nguyen)';
                }
                return `Từ "${word}" phải bắt đầu bằng chữ hoa (ví dụ: Nguyễn Văn A)`;
            }
        }

        return null;
    }

    function getOrCreateErrorEl(input) {
        let el = input.parentElement.querySelector('[data-name-err]');
        if (!el) {
            el = document.createElement('p');
            el.setAttribute('data-name-err', '1');
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
        errorEl.style.color = '#dc3545';
        errorEl.textContent = msg;
        errorEl.style.display = 'block';
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
        const msgEl = getOrCreateErrorEl(input);
        let touched = false;

        input.addEventListener('blur', function () {
            touched = true;
            if (this.value.trim() === '') { clearMsg(this, msgEl); return; }
            const err = getNameError(this.value);
            if (err) showError(this, msgEl, err);
            else showSuccess(this, msgEl);
        });

        input.addEventListener('input', function () {
            if (!touched) return;
            if (this.value.trim() === '') { clearMsg(this, msgEl); return; }
            const err = getNameError(this.value);
            if (err) showError(this, msgEl, err);
            else showSuccess(this, msgEl);
        });

        const form = input.closest('form');
        if (form) {
            form.addEventListener('submit', function (e) {
                touched = true;
                const err = getNameError(input.value);
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
        document.querySelectorAll(NAME_SELECTORS).forEach(function (input) {
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