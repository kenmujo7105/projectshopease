import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import EmptyState from './EmptyState';

describe('EmptyState component', () => {
  it('renders default title correctly', () => {
    render(<EmptyState />);
    expect(screen.getByText('Không có dữ liệu')).toBeInTheDocument();
  });

  it('renders custom title and description', () => {
    render(<EmptyState title="Lỗi tải dữ liệu" description="Vui lòng thử lại sau" />);
    expect(screen.getByText('Lỗi tải dữ liệu')).toBeInTheDocument();
    expect(screen.getByText('Vui lòng thử lại sau')).toBeInTheDocument();
  });
});
