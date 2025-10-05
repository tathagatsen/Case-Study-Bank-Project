// components/Sidebar.js
import {
  CalendarIcon,
  ChartBarIcon,
  FolderIcon,
  HomeIcon,
  InboxIcon,
  UsersIcon,
  CashIcon
} from '@heroicons/react/outline'

import { useLocation } from 'react-router'


const navigation = [
  { name: 'Dashboard', href: '/dashboard', icon: UsersIcon },
  { name: 'Accounts', href: '/account', icon: UsersIcon },
  { name: 'Transaction', href: '/transaction', icon: CashIcon },
  // { name: 'Bill Payment', href: '/bill', icon: FolderIcon },
  { name: 'Deposit', href: '/deposit', icon: CalendarIcon },
  { name: 'Help & Support', href: '/support', icon: InboxIcon },
  { name: 'Kyc', href: '/kyc', icon: InboxIcon },
];
function classNames(...classes) {
  return classes.filter(Boolean).join(' ')
}

export default function Sidebar() {

  const location = useLocation();
const currentPath = location.pathname;
  return (
    <div className="flex flex-col flex-grow pt-5 bg-indigo-700 overflow-y-auto">
      <div className="flex items-center flex-shrink-0 px-4">
        <img
          className="h-8 w-auto"
          src="https://tailwindui.com/img/logos/workflow-logo-indigo-300-mark-white-text.svg"
          alt="Workflow"
        />
      </div>
      <div className="mt-5 flex-1 flex flex-col">
        <nav className="flex-1 px-2 pb-4 space-y-1">
          {navigation.map((item) => {
  const isActive = currentPath === item.href;

  return (
    <a
      key={item.name}
      href={item.href}
      className={classNames(
        isActive ? 'bg-indigo-800 text-white' : 'text-indigo-100 hover:bg-indigo-600',
        'group flex items-center px-2 py-2 text-sm font-medium rounded-md'
      )}
    >
      <item.icon
        className={classNames(
          isActive ? 'text-white' : 'text-indigo-300',
          'mr-3 flex-shrink-0 h-6 w-6'
        )}
        aria-hidden="true"
      />
      {item.name}
    </a>
  );
})}

        </nav>
      </div>
    </div>
  )
}
