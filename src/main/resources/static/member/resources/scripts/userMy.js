const infoForm = window.document.getElementById('infoForm');
const truncateForm = window.document.getElementById('truncateForm');
const profileForm = window.document.getElementById('profileForm');
const profileNicknameInput = window.document.getElementById('profileNicknameInput');
const profileNicknameInputCheck = window.document.getElementById('profileNicknameInputCheck');
const profileNicknameDefault = document.querySelector('.profile-nickname-default');
const profileNicknameCheck = document.querySelector('.profile-nickname-check');
const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};
const checkboxes = document.querySelectorAll('.truncate-form input[type="checkbox"]');


const truncateButtonController = {
    getElement: () => window.document.getElementById('truncateButton'),
    hide: () => truncateButtonController.getElement().classList.remove('visible'),
    show: () => truncateButtonController.getElement().classList.add('visible')
}

const contentInput = document.querySelector('#truncateForm input[name="content"]');
if (contentInput) {
    contentInput.addEventListener('input', () => {
        const inputValue = contentInput.value.trim();
        inputValue !== '' ? truncateButtonController.show() : truncateButtonController.hide();
    });
}
const handleCheckboxChange = (clickedCheckbox) => {
    const index = parseInt(clickedCheckbox.getAttribute('data-index'));
    checkboxes.forEach((checkbox, i) => {
        if (i !== index) {
            checkbox.checked = false;
        }
    });
    // 하나 이상의 체크박스가 선택되었는지 확인
    const atLeastOneChecked = Array.from(checkboxes).some((checkbox) => checkbox.checked);
    // 버튼을 표시 또는 숨김
    if (atLeastOneChecked) {
        truncateButtonController.show();
    } else {
        truncateButtonController.hide();
    }
};

profileNicknameInput?.addEventListener('click', ()=> {
    profileNicknameDefault.classList.remove('visible');
    profileNicknameCheck.classList.add('visible');
    profileForm['profileNickname'].removeAttribute('disabled');
});

profileNicknameInputCheck?.addEventListener('click', ()=> {
    if (profileForm['profileNickname'].value === '') {
        alert('새로운 닉네임을 입력해 주세요.');
        profileForm['profileNickname'].focus();
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('profileNickname', profileForm['profileNickname'].value);
    xhr.open('POST', `./userMyInfoNickname`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        profileNicknameDefault.classList.add('visible');
                        profileNicknameCheck.classList.remove('visible');
                        profileForm['profileNickname'].setAttribute('disabled', 'disabled');
                        break;
                    default:
                     alert('닉네임을 변경하는데 실패하였습니다. 잠시 후 다시 시도해주세요.\n\n 해당 문제가 계속해서 발생될 시 문의해주시기 바랍니다.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
});

checkboxes?.forEach((checkbox) => {
    checkbox.addEventListener('change', () => handleCheckboxChange(checkbox));
});

// profileForm?.changeNickname?.addEventListener('input', () => {
//     infoForm.querySelectorAll('[rel="row-change-nick"]').forEach(x => {
//         if (infoForm['changeNickname'].checked) {
//             x.classList.add('visible');
//             infoForm['newNickname'].value = '';
//             infoForm['newNickname'].focus();
//         } else {
//             x.classList.remove('visible');
//         }
//     });
// });

infoForm?.changePassword?.addEventListener('input', () => {
    infoForm.querySelectorAll('[rel="row-change-password"]').forEach(x => {
        if (infoForm['changePassword'].checked) {
            x.classList.add('visible');
            infoForm['newPassword'].value = '';
            infoForm['newPasswordCheck'].value = '';
            infoForm['newPassword'].focus();
        } else {
            x.classList.remove('visible');
        }
    });
});

infoForm?.changeContact?.addEventListener('input', () => {
    infoForm.querySelectorAll('[rel="row-change-contact"]').forEach(x => {
        if (infoForm['changeContact'].checked) {
            x.classList.add('visible');
            infoForm['newContactAuthSalt'].value = '';
            infoForm['newContact'].value = '';
            infoForm['newContact'].removeAttribute('disabled');
            infoForm['newContactAuthRequestButton'].removeAttribute('disabled');
            infoForm['newContactAuthCode'].value = '';
            infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
            infoForm['newContactAuthCheckButton'].setAttribute('disabled', 'disabled');
            infoForm['newContact'].focus();
        } else {
            x.classList.remove('visible');
        }
    });
});

infoForm?.newContactAuthRequestButton?.addEventListener('click', () => {
    warning.hide();
    if (infoForm['newContact'].value === '') {
        warning.show('새로운 연락처를 입력해 주세요.');
        infoForm['newContact'].focus();
        return;
    }
    if (!new RegExp('^(\\d{8,12})$').test(infoForm['newContact'].value)) {
        warning.show('올바른 새로운 연락처를 입력해 주세요.')
        infoForm['newContact'].focusAndSelect();
        return;
    }
    cover.show('인증번호를 전송하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    xhr.open('GET', `./userMyInfoAuth?newContact=${infoForm['newContact'].value}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'duplicate':
                        warning.show('해당 연락처는 이미 사용 중입니다.');
                        infoForm['newContact'].focusAndSelect();
                        break;
                    case 'success':
                        warning.show('입력하신 연락처로 인증번호를 포함한 문자를 전송하였습니다. 5분 내로 문자로 전송된 인증번호를 확인해 주세요.');
                        infoForm['newContactAuthSalt'].value = responseJson['salt'];
                        infoForm['newContact'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthRequestButton'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthCode'].removeAttribute('disabled');
                        infoForm['newContactAuthCheckButton'].removeAttribute('disabled');
                        infoForm['newContactAuthCode'].focusAndSelect();
                        break;
                    default:
                        warning.show('알 수 없는 이유로 인증번호를 전송하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                        infoForm['newContact'].focusAndSelect();
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                infoForm['newContact'].focusAndSelect();
            }
        }
    };
    xhr.send();
});

// const profileImageInput = document.querySelector('#infoForm input[name="profileImage"]');
// if (profileImageInput) {
//     profileImageInput.addEventListener('input', () => {
//         const xhr = new XMLHttpRequest();
//         const formData = new FormData();
//         if ((infoForm['profileImage'].files?.length ?? 0) > 0) {
//             formData.append('profileImage', infoForm['profileImage'].files[0]);
//         }
//         xhr.open('POST', './userMyInfoProfileImage');
//         xhr.onreadystatechange = () => {
//             if (xhr.readyState === XMLHttpRequest.DONE) {
//                 if (xhr.status >= 200 && xhr.status < 300) {
//                     const responseJson = JSON.parse(xhr.responseText);
//                     switch (responseJson['result']) {
//                         case 'success':
//                             alert('성공적으로 이미지를 변경했습니다.');
//                             window.location.reload();
//                             break;
//                         default:
//                             warning.show('알 수 없는 이유로 이미지를 변경 할 수 없습니다. 다시 확인해 주세요.');
//                     }
//                 } else {
//                     warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
//                 }
//             }
//         };
//         xhr.send(formData);
//     });
// }

const profileImageInput = document.querySelector('#profileForm input[name="profileImage"]');
if (profileImageInput) {
    profileImageInput.addEventListener('input', () => {
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        if ((profileForm['profileImage'].files?.length ?? 0) > 0) {
            formData.append('profileImage', profileForm['profileImage'].files[0]);
        }
        xhr.open('POST', './userMyInfoProfileImage');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('성공적으로 이미지를 변경했습니다.');
                            window.location.reload();
                            break;
                        default:
                            warning.show('알 수 없는 이유로 이미지를 변경 할 수 없습니다. 다시 확인해 주세요.');
                    }
                } else {
                    warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    });
}

infoForm?.newContactAuthCheckButton?.addEventListener('click', () => {
    warning.hide();
    if (infoForm['newContactAuthCode'].value === '') {
        warning.show('인증번호를 입력해 주세요.');
        infoForm['newContactAuthCode'].focus();
        return;
    }
    if (!new RegExp('^(\\d{6})$').test(infoForm['newContactAuthCode'].value)) {
        warning.show('올바른 인증번호를 입력해 주세요.');
        infoForm['newContactAuthCode'].focusAndSelect();
        return;
    }
    cover.show('인증번호를 확인하고 있습니다.\n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('newContact', infoForm['newContact'].value);
    formData.append('newContactAuthCode', infoForm['newContactAuthCode'].value);
    formData.append('newContactAuthSalt', infoForm['newContactAuthSalt'].value);
    xhr.open('POST', './userMyInfoAuth');
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'expired':
                        warning.show('입력한 인증번호가 만료되었습니다. 인증번호를 다시 요청하여 인증해 주세요.');
                        infoForm['newContact'].removeAttribute('disabled');
                        infoForm['newContactAuthRequestButton'].removeAttribute('disabled');
                        infoForm['newContactAuthCode'].value = '';
                        infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthCheckButton'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthSalt'].value = '';
                        infoForm['newContact'].focusAndSelect();
                        break;
                    case 'success':
                        warning.show('연락처가 성공적으로 인증되었습니다.');
                        infoForm['newContactAuthCode'].setAttribute('disabled', 'disabled');
                        infoForm['newContactAuthCheckButton'].setAttribute('disabled', 'disabled');
                        break;
                    default:
                        warning.show('입력한 인증번호가 올바르지 않습니다.');
                        infoForm['newContactAuthCode'].focusAndSelect();
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                infoForm['newContact'].focusAndSelect();
            }
        }
    };
    xhr.send(formData);
});

if (infoForm !== null) {
    infoForm.onsubmit = e => {
        e.preventDefault();

        if (!infoForm['changePassword'].checked && !infoForm['changeContact'].checked) {
            warning.show('변경할 내용이 없습니다.');
            return false;
        }
        if (infoForm['oldPassword'].value === '') {
            warning.show('현재 비밀번호를 입력해 주세요.');
            infoForm['oldPassword'].focus();
            return false;
        }

        if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(infoForm['oldPassword'].value)) {
            warning.show('올바른 현재 비밀번호를 입력해 주세요.');
            infoForm['oldPassword'].focusAndSelect();
            return false;
        }
        if (infoForm['changePassword'].checked) {
            if (infoForm['newPassword'].value === '') {
                warning.show('새로운 비밀번호를 입력해 주세요.');
                infoForm['newPassword'].focus();
                return false;
            }
            if (!new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'\",<.>/?]{8,50})$').test(infoForm['newPassword'].value)) {
                warning.show('올바른 새로운 비밀번호를 입력해 주세요.');
                infoForm['newPassword'].focusAndSelect();
                return false;
            }
            if (infoForm['newPasswordCheck'].value === '') {
                warning.show('새로운 비밀번호를 다시 입력해 주세요.');
                infoForm['newPasswordCheck'].focus();
                return false;
            }
            if (infoForm['newPassword'].value !== infoForm['newPasswordCheck'].value) {
                warning.show('새로운 비밀번호가 서로 일치하지 않습니다.');
                infoForm['newPasswordCheck'].focusAndSelect();
                return false;
            }
        }
        if (infoForm['changeContact'].checked) {
            if (!infoForm['newContactAuthRequestButton'].disabled || !infoForm['newContactAuthCheckButton'].disabled) {
                warning.show('변경할 새로운 연락처에 대한 인증을 완료해 주세요.');
                return false;
            }
        }
        cover.show('회원정보를 변경하고 있습니다.\n\n잠시만 기다려 주세요.');
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('oldPassword', infoForm['oldPassword'].value);
        formData.append('changePassword', infoForm['changePassword'].checked);
        formData.append('changeContact', infoForm['changeContact'].checked);

        if (infoForm['changePassword'].checked) {
            formData.append('newPassword', infoForm['newPassword'].value);
        }
        if (infoForm['changeContact'].checked) {
            formData.append('newContact', infoForm['newContact'].value);
            formData.append('newContactAuthCode', infoForm['newContactAuthCode'].value);
            formData.append('newContactAuthSalt', infoForm['newContactAuthSalt'].value);
        }
        xhr.open('POST', './userMyInfo');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':
                            alert('회원정보를 성공적으로 수정하였습니다.');
                            window.location.reload();
                            break;
                        default:
                            warning.show('현재 비밀번호가 일치하지 않습니다. 다시 확인해 주세요.');
                            infoForm['oldPassword'].focusAndSelect();
                    }
                } else {
                    warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            }
        };
        xhr.send(formData);
    };
}


if (truncateForm !== null) {
    truncateForm.onsubmit = e => {
        e.preventDefault();
        if (truncateForm['content'].value === '') {
            alert('탈퇴하려는 이유를 알려주세요.');
            truncateForm['content'].focus();
            return;
        }
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('findAccompany', truncateForm['findAccompany'].checked);
        formData.append('tripEnds', truncateForm['tripEnds'].checked);
        formData.append('travelProducts', truncateForm['travelProducts'].checked);
        formData.append('badManners', truncateForm['badManners'].checked);
        formData.append('inconvenience', truncateForm['inconvenience'].checked);
        formData.append('new', truncateForm['new'].checked);
        formData.append('useful', truncateForm['useful'].checked);
        formData.append('content', truncateForm['content'].value);
        xhr.open('DELETE', './userMyInfoDelete');
        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                cover.hide();
                if (xhr.status >= 200 && xhr.status < 300) {
                    const responseJson = JSON.parse(xhr.responseText);
                    switch (responseJson['result']) {
                        case 'success':

                            window.location.href = `/`
                            break
                        default:
                            alert('회원탈퇴에 실패 하셨습니다. 다시 시도해주세요.');
                            break
                    }
                } else {
                    warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                    infoForm['newContact'].focusAndSelect();
                }
            }
        };
        xhr.send(formData);
    }
}
;

const accompany = window.document.querySelectorAll('.accompany');
const accompanyArray = Array.from(accompany);

accompanyArray.forEach(article => {
    const currentDate = new Date();
    const dateFromElement = article.querySelector('.date-From');
    const dateString = dateFromElement.innerText;

    const dateObject = new Date(currentDate.getFullYear(), parseInt(dateString.split('-')[0]) - 1, parseInt(dateString.split('-')[1]));

    const status = currentDate > dateObject ? 'expired' : 'status';

    const statusElement = window.document.createElement('span');
    statusElement.classList.add('status');

    if (status === 'expired') {
        statusElement.classList.add('expired');
        statusElement.innerText = '모집 마감';
    } else {
        statusElement.innerText = '모집중';
    }

    const titleContainerElement = article.querySelector('.title-container');
    titleContainerElement.prepend(statusElement);
});







