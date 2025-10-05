// ./ui/Button.js
import React from 'react';
import clsx from 'clsx';

const VARIANT_CLASSES = {
  primary: 'bg-blue-600 text-white hover:bg-blue-700',
  secondary: 'bg-white text-gray-900 border border-gray-300 hover:bg-gray-50',
  danger: 'bg-red-600 text-white hover:bg-red-700',
};

const Button = ({
  children,
  type = 'button',
  onClick,
  className = '',
  variant = 'primary',
  ...props
}) => (
  <button
    type={type}
    onClick={onClick}
    className={clsx(
      'inline-flex items-center px-4 py-2 rounded-md font-medium focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition',
      VARIANT_CLASSES[variant] || VARIANT_CLASSES.primary,
      className
    )}
    {...props}
  >
    {children}
  </button>
);

export default Button;