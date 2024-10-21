export function changePlaceholderOnFocus(inputElement: HTMLInputElement, focusPlaceholder: string, blurPlaceholder: string) {
    // Change placeholder on focus
    inputElement.addEventListener('focus', () => {
      inputElement.placeholder = focusPlaceholder;
    });
  
    // Revert placeholder on blur
    inputElement.addEventListener('blur', () => {
      inputElement.placeholder = blurPlaceholder;
    });
  }
  