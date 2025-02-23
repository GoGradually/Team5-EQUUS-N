import { TeamProvider } from './TeamContext';
import { UserProvider } from './UserContext';

const CombinedProvider = ({ children }) => {
  return (
    <TeamProvider>
      <UserProvider>{children}</UserProvider>
    </TeamProvider>
  );
};

export default CombinedProvider;
