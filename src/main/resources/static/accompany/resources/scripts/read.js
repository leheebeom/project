const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

const coverImage = window.document.getElementById('coverImage');
const title = window.document.getElementById('title');
const region = window.document.getElementById('region');
const capacity = window.document.getElementById('capacity');
const dateFrom = window.document.getElementById('dateFrom');
const dateTo = window.document.getElementById('dateTo');
const content = window.document.getElementById('content');
const createdAt = window.document.getElementById('createdAt');
const nickname = window.document.getElementById('nickname');
const requestButton = window.document.getElementById('requestButton');
const retractButton = window.document.getElementById('retractButton');
const deleteButton = window.document.getElementById('deleteButton');
const modifyButton = window.document.getElementById('modifyButton');
const viewCount = window.document.getElementById('viewCount');
const contentForm = window.document.getElementById('contentForm');
const replyForm = window.document.getElementById('replyForm');
const replyButtonAll = window.document.querySelectorAll('.reply-button');
const parentIndex = window.document.getElementById('number');
const commentModifyButtonAll = window.document.querySelectorAll('.reply-container > .modify-button');
const commentDeleteButtonAll = window.document.querySelectorAll('.reply-container > .delete-button');
const greatButtonAll = window.document.querySelectorAll('.great-container > .great-button');
const greatHitButtonAll = window.document.querySelectorAll('.great-container > .great-hit-button');


const showForm = form => form.classList.add('visible');
const hideForm = form => form.classList.remove('visible');
// 초기 상태 설정
showForm(contentForm);
hideForm(replyForm);

const checkRequest = () => {
    cover.show('서버와 통신 중입니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('GET', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                if (responseJson['result'] === true) {
                    //요청한적 있음.
                    retractButton.classList.add('visible');
                } else {
                    //요청한적 없음
                    requestButton.classList.add('visible');
                }
            } else {
                alert('일부 정보를 불러오지 못하였습니다.');
            }
        }
    };
    xhr.send();
};

const loadComment = () => {
    cover.show('댓글 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.')
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('commentIndex', contentForm['commentIndex'].value);
    xhr.open('PATCH', window.location.href);
    xhr.onreadystatechange = () => {
        if(xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide()
            if(xhr.status >= 200 && xhr.status <300) {
                const responseJson = JSON.parse(xhr.responseText);
                contentForm['content'].value = responseJson['content'];
                contentForm['commentIndex'].value = responseJson['commentIndex'];
            } else if(xhr.status === 403) {
                alert('로그인 정보가 유효하지 않습니다.')
            } else if(xhr.status === 404) {
                alert('존재하지 않는 코멘트입니다.')
                if (window.history.length > 0) {
                    window.history.back();
                } else {
                    window.close();
                }
            }
        }
    };
    xhr.send(formData);
};


const xhr = new XMLHttpRequest();
cover.show('요청한 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.')
xhr.open('POST', window.location.href);
xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE) {
        cover.hide();
        if (xhr.status >= 200 && xhr.status < 300) {
            const responseJson = JSON.parse(xhr.responseText);
            const createdAtObj = new Date(responseJson['createdAt']);
            const dateFromObj = new Date(responseJson['dateFrom']);
            const dateToObj = new Date(responseJson['dateTo']);
            coverImage.setAttribute('src', `../cover-image/${id}`);
            title.innerText = responseJson['title'];
            window.document.title = `${responseJson['title']} :: 드립소다`;
            region.innerText = responseJson['regionValue'];
            capacity.innerText = `${responseJson['capacity']}명`;
            dateFrom.innerText = `${dateFromObj.getFullYear()} - ${dateFromObj.getMonth() + 1} - ${dateFromObj.getDate()}`;
            dateTo.innerText = `${dateToObj.getFullYear()} - ${dateToObj.getMonth() + 1} - ${dateToObj.getDate()}`;
            content.innerHTML = responseJson['content'];
            nickname.innerText = responseJson['userNickname'];
            if ((responseJson['mine'] ?? false) === true) {
                modifyButton.classList.add('visible');
                deleteButton.classList.add('visible');
            }
            viewCount.innerText = `조회수 ${responseJson['viewCount']}`;
            createdAt.innerText = `${createdAtObj.getFullYear()}-${createdAtObj.getMonth() + 1}-${createdAtObj.getDate()} -${createdAtObj.getHours()}:${createdAtObj.getMinutes()}`;
            checkRequest();

        } else if (xhr.status === 404) {
            alert('존재하지 않는 동행 게시글입니다.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        } else {
            alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            if (window.history.length > 0) {
                window.history.back();
            } else {
                window.close();
            }
        }

    }
};
xhr.send();

deleteButton.addEventListener('click', () => {
    if (!confirm('정말로 동행을 삭제할까요?')) {
        return;
    }
    cover.show('동행을 삭제하고 있습니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('동행을 성공적으로 삭제하였습니다.');
                        window.location.href = `../`;
                        break;
                    case 'k' :
                        alert('k');
                        break;
                    default :
                        alert('알 수 없는 이유로 동행을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});

modifyButton.addEventListener('click', () => {
    window.location.href = `../modify/${id}`;
});


requestButton.addEventListener('click', () => {
    if (requestButton.dataset.signed === 'false') {
        window.location.href = '../../member/userLogin';
        return;
    }
    cover.show('동행 신청 처리 중입니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('POST', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 동행 정보입니다.');
                        window.location.href = '../';
                        break;
                    case 'not_signed' :
                        alert('로그인 정보가 유효하지 않습니다.');
                        break
                    case 'yourself' :
                        alert('나야나');
                        break
                    case 'success' :
                        alert('동행 신청에 성공하였습니다.');
                        window.location.reload();
                        break;
                    default :
                        alert('알 수 없는 이유로 동행 신청에 실패하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});

retractButton.addEventListener('click', () => {
    cover.show('동행 취소 처리 중입니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', `../request/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 동행 정보입니다.');
                        window.location.href = '../';
                        break;
                    case 'not_signed' :
                        alert('로그인 정보가 유효하지 않습니다.');
                        break
                    case 'success' :
                        alert('동행 신청 취소에 성공하였습니다.');
                        window.location.reload();
                        break;
                    default :
                        alert('알 수 없는 이유로 동행 신청에 실패하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});


const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};
const handleModifyButtonClick = (commentIndex) => {
    // content-form에 modify 클래스 추가
    contentForm.classList.add('modify');

};
const setDefaultFormState = () => {
    // content-form에 modify 클래스 제거
    contentForm.classList.remove('modify');

    // 여기에 필요한 기본 동작을 추가할 수 있습니다.
};

let commentId

contentForm.onsubmit = e => {
    e.preventDefault();
    if (contentForm['content'].value === '') {
        contentForm['content'].focus();
        alert('댓글을 작성해주세요.');
        return false;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('content', contentForm['content'].value);
    const url = contentForm.classList.contains('modify')
        ? `../modify/${id}/${commentId}`
        : `../read/${id}/comments`;
    xhr.open('POST', url);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        window.location.href = `../read/${id}`;
                        break
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 댓글 정보입니다.');
                        window.location.href = `../read/${id}`;
                        break;
                    case 'not_signed' :
                        alert('로그인정보가 유효하지 않습니다.');
                        break
                    default:
                        warning.show('알 수 없는 이유로 댓글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};


replyButtonAll.forEach((reply) => {
    // Adding a click event listener to each reply button
    reply.addEventListener('click', () => {
        showForm(replyForm);
        hideForm(contentForm);
        replyForm['content'].focus();
        commentId = reply.dataset.index;
        const commentUserNickname = reply.closest('.comment-container').querySelector('.nickname').innerText;
        replyForm['content'].value = `@${commentUserNickname} `;
    });
});


commentModifyButtonAll.forEach((modify) => {
    modify.addEventListener('click', () => {
        showForm(contentForm);
        hideForm(replyForm);
        let commentIndex = modify.getAttribute('data-index');
        // Change from 'button' to 'modify'
        setDefaultFormState();
        handleModifyButtonClick(commentIndex);
        let parseCommentIndex =  parseInt(modify.getAttribute('data-index'), 10);
        commentId = parseCommentIndex;
        let commentElement = document.querySelector('.comment[data-comment="' + commentIndex + '"]');
        contentForm['commentIndex'].value = parseCommentIndex;
        // Set the comment content in the contentForm
        document.querySelector('#contentForm textarea[name="content"]').value = commentElement.innerText;
        document.querySelector('#contentForm textarea[name="content"]').focus(); // Use 'document.querySelector' instead of 'contentForm'
        loadComment();

    });
});

setDefaultFormState();
// const toggleGreatVisibility = (button, hitButton, isHitButton) => {
//     if (!button || !hitButton) return;
//
//     // Toggle visibility
//     button.style.display = isHitButton ? 'none' : 'inline-block';
//     hitButton.style.display = isHitButton ? 'inline-block' : 'none';
// };

const createHitHeartSVG = () => {
    const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute('width', '13');
    svg.setAttribute('height', '13');
    svg.setAttribute('viewBox', '0 0 20 20');
    svg.setAttribute('fill', 'none');
    svg.classList.add('great-hit-heart');

    const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
    path.setAttribute('fill-rule', 'evenodd');
    path.setAttribute('clip-rule', 'evenodd');
    path.setAttribute('d', 'M1.65346 2.62939C-0.551152 4.80192 -0.551153 8.32428 1.65346 10.4968L2.01647 10.8545L2.01636 10.8546L9.28769 18.0201C9.68107 18.4078 10.3189 18.4078 10.7122 18.0201L18.3465 10.497C20.5511 8.3245 20.5511 4.80214 18.3465 2.62962C16.1418 0.457092 12.5675 0.45709 10.3629 2.62962L10.0001 2.98712L9.63706 2.62939C7.43245 0.456868 3.85807 0.456869 1.65346 2.62939Z');
    path.setAttribute('fill', '#E98670');

    svg.appendChild(path);
    return svg;
};


greatButtonAll.forEach((great) => {
   great.addEventListener('click', () =>{
      let commentIndex = great.getAttribute('data-index');
      commentId = parseInt(commentIndex);
      const xhr = new XMLHttpRequest();
      xhr.open('PUT', `../like/${id}/${commentId}`);
       xhr.onreadystatechange = () => {
           if (xhr.readyState === XMLHttpRequest.DONE) {
               cover.hide();
               if (xhr.status >= 200 && xhr.status < 300) {
                   const responseJson = JSON.parse(xhr.responseText);
                   switch (responseJson['result']) {
                       case 'success':
                           window.location.href = `../read/${id}`;
                           break;
                       case 'not_found' :
                           window.location.href = `../read/${id}`;
                           break;
                       case 'not_signed' :
                           alert('로그인 정보가 유효하지 않습니다.');
                           break
                       default :
                           alert('알 수 없는 이유로 좋아요를 누를 수 없습니다.');
                   }
               } else {
                   alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
               }
           }
       };
       xhr.send();
   });
});

greatHitButtonAll.forEach((greatHit) => {
   greatHit.addEventListener('click', () => {
       let commentIndex = greatHit.getAttribute('data-index');
       commentId = parseInt(commentIndex);
       const xhr = new XMLHttpRequest();
       xhr.open('DELETE', `../like/${id}/${commentId}`);
       xhr.onreadystatechange = () => {
           if (xhr.readyState === XMLHttpRequest.DONE) {
               cover.hide();
               if (xhr.status >= 200 && xhr.status < 300) {
                   const responseJson = JSON.parse(xhr.responseText);
                   switch (responseJson['result']) {
                       case 'success':
                           window.location.href = `../read/${id}`;
                           break;
                       case 'not_found' :
                           window.location.href = `../read/${id}`;
                           break;
                       case 'not_signed' :
                           alert('로그인 정보가 유효하지 않습니다.');
                           break
                       default :
                           alert('알 수 없는 이유로 좋아요 삭제를 할 수 없습니다.');
                   }
               } else {
                   alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
               }
           }
       };
       xhr.send();
   });
});

commentDeleteButtonAll.forEach((deleteComment) => {
   deleteComment.addEventListener('click', () => {
       let deleteCommentIndex = deleteComment.getAttribute('data-index');
       commentId = parseInt(deleteCommentIndex);
       if (!confirm('정말로 댓글을 삭제할까요?')) {
           return;
       }
       cover.show('댓글을 삭제하고 있습니다.\n\n잠시만 기다려 주세요.');
       const xhr = new XMLHttpRequest();
       xhr.open('DELETE', `../delete/${id}/${commentId}`);
       xhr.onreadystatechange = () => {
           if (xhr.readyState === XMLHttpRequest.DONE) {
               cover.hide();
               if (xhr.status >= 200 && xhr.status < 300) {
                   const responseJson = JSON.parse(xhr.responseText);
                   switch (responseJson['result']) {
                       case 'success':
                           alert('댓글을 성공적으로 삭제하였습니다.');
                           window.location.href = `../read/${id}`;
                           break;
                       case 'not_found' :
                           alert('더 이상 존재하지 않는 댓글 정보입니다.');
                           window.location.href = `../read/${id}`;
                           break;
                       case 'not_signed' :
                           alert('로그인 정보가 유효하지 않습니다.');
                           break
                       default :
                           alert('알 수 없는 이유로 댓글을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                   }
               } else {
                   alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
               }
           }
       };
       xhr.send();
   })
});

replyForm.addEventListener('input', () => {
    const replyContent = replyForm['content'].value.trim();
    // 답글 내용이 비어 있거나 '@'로 시작하지 않을 경우
    if (replyContent === '' || !replyContent.startsWith('@')) {
        replyButtonAll.forEach((reply) => {
            reply.dataset.index = commentId; // 댓글 버튼으로 변경
            showForm(contentForm);
            hideForm(replyForm);
        });
    } else {
        // '@'로 시작하는 경우, 서버에 제출되지 않도록 설정
        replyButtonAll.forEach((reply) => {
            reply.dataset.index = ''; // 서버에 전송하지 않음
        });
    }
});
replyForm.onsubmit = e => {
    e.preventDefault();
    const replyContent = replyForm['content'].value.trim();
    // 정규 표현식을 사용하여 @닉네임(공백) 부분을 지운 내용 추출
    const nicknameMatch = /^@(\S+)\s*(.*)/.exec(replyContent);
    const contentWithoutNickname = nicknameMatch ? nicknameMatch[2] : replyContent;
    // 답글이 비어있는 경우
    if (contentWithoutNickname === '') {
        alert('답글을 작성해주세요.');
        return false;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('content', contentWithoutNickname);
    xhr.open('POST', `./${id}/reply/${commentId}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        window.location.href = `../read/${id}`;
                        break
                    case 'not_found' :
                        alert('더 이상 존재하지 않는 댓글 정보입니다.');
                        window.location.href = `../read/${id}`;
                        break;
                    case 'not_signed' :
                        alert('로그인정보가 유효하지 않습니다.');
                        break
                    default:
                        warning.show('알 수 없는 이유로 답글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};



